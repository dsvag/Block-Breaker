package dev.vpestryakov.block_breaker.ui.field

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.vpestryakov.block_breaker.CrashReporter
import dev.vpestryakov.block_breaker.GamePreferences
import dev.vpestryakov.block_breaker.dto.Block
import dev.vpestryakov.block_breaker.dto.Blocks
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class FieldViewModel(
  private val preferences: GamePreferences,
  private val crashReporter: CrashReporter,
) : ViewModel() {

  val game =
    preferences.gameFlow.stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = null,
    )

  val effects: SharedFlow<Effect>
    field = MutableSharedFlow<Effect>()

  fun resetGame() {
    viewModelScope.launch {
      crashReporter.log("Game reset")
      runCatching {
        preferences.reset()
        effects.emit(Effect.ResetPosition)
      }.onFailure { throwable ->
        if (throwable is CancellationException) throw throwable
        crashReporter.recordException(throwable)
      }
    }
  }

  fun findPlacementTarget(
    fieldBounds: IntRect,
    blockBounds: IntRect,
    cellSize: Float,
    spacing: Float,
    contentPadding: Float,
    blockIndex: Int,
  ): IntOffset? {
    val currentGame = game.value ?: return null
    val block = currentGame.blocks.get(blockIndex) ?: return null

    val topLeft =
      findMatchingCellIndex(
        fieldBounds = fieldBounds,
        blockBounds = blockBounds,
        block = block,
        cellSize = cellSize,
        spacing = spacing,
        contentPadding = contentPadding,
        dimension = currentGame.field.dimension,
      ) ?: return null

    if (!currentGame.field.canPlaceBlockAt(block = block, coordinates = topLeft)) {
      return null
    }

    return topLeft
  }

  fun commitPlaceBlock(blockIndex: Int, coordinates: IntOffset) {
    viewModelScope.launch {
      runCatching {
        placeBlock(blockIndex = blockIndex, coordinates = coordinates)
      }.onFailure { throwable ->
        if (throwable is CancellationException) throw throwable
        crashReporter.recordException(throwable)
      }
    }
  }

  private suspend fun placeBlock(blockIndex: Int, coordinates: IntOffset) {
    var hasClearedLines = false

    preferences.updateGame { current ->
      val block = current.blocks.get(blockIndex) ?: return@updateGame current
      if (!current.field.canPlaceBlockAt(block = block, coordinates = coordinates)) {
        return@updateGame current
      }

      val placedField = current.field.placeBlock(block = block, coordinates = coordinates)
      val (_, clearedLines) = placedField.clearFilledLines()
      hasClearedLines = clearedLines > 0

      val remainingBlocks = current.blocks.remove(blockIndex)
      val nextBlocks = if (remainingBlocks.isEmpty) Blocks.random() else remainingBlocks

      current.copy(
        field = placedField,
        blocks = nextBlocks,
        score = current.score + block.cellsCount,
      )
    }

    if (hasClearedLines) {
      kotlinx.coroutines.delay(50.milliseconds)
      preferences.updateGame { current ->
        val (clearedField, clearedLines) = current.field.clearFilledLines()
        if (clearedLines == 0) return@updateGame current
        val lineBonus = clearedLines * current.field.dimension

        current.copy(
          field = clearedField,
          score = current.score + lineBonus,
        )
      }
    }
  }

  private fun findMatchingCellIndex(
    fieldBounds: IntRect,
    blockBounds: IntRect,
    block: Block,
    cellSize: Float,
    spacing: Float,
    contentPadding: Float,
    dimension: Int,
  ): IntOffset? {
    val edgeTolerance = (cellSize * 0.75F).roundToInt()
    val expandedFieldBounds =
      IntRect(
        left = fieldBounds.left - edgeTolerance,
        top = fieldBounds.top - edgeTolerance,
        right = fieldBounds.right + edgeTolerance,
        bottom = fieldBounds.bottom + edgeTolerance,
      )
    if (!expandedFieldBounds.contains(blockBounds.center)) return null

    val offset = blockBounds.topLeft - fieldBounds.topLeft
    val effectiveSize = cellSize + spacing

    val rawCol = ((offset.x - contentPadding) / effectiveSize).roundToInt()
    val rawRow = ((offset.y - contentPadding) / effectiveSize).roundToInt()

    val maxCol = (dimension - block.size.width).coerceAtLeast(0)
    val maxRow = (dimension - block.size.height).coerceAtLeast(0)

    if (rawCol !in -1..(maxCol + 1) || rawRow !in -1..(maxRow + 1)) return null

    val col = rawCol.coerceIn(0, maxCol)
    val row = rawRow.coerceIn(0, maxRow)

    return IntOffset(x = col, y = row)
  }
}

sealed interface Effect {

  data object ResetPosition : Effect
}

