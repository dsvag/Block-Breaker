package dev.vpestryakov.block_breaker

import android.app.Application
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.vpestryakov.block_breaker.dto.Block
import dev.vpestryakov.block_breaker.dto.Field
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FieldViewModel(application: Application) :
  ViewModel(viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())) {

  private val preferences = GamePreferences(application.dataStore)

  private val scoreFlow = preferences.scoreFlow
  val score = scoreFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0L)

  private val fieldFlow = preferences.fieldFlow
  val field = fieldFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Field.Empty)

  private val blockFlow = MutableStateFlow(Block.random())
  val block = blockFlow.asStateFlow()

  private val dialogFlow = MutableStateFlow(false)
  val dialog = dialogFlow.asStateFlow()

  private val effectsFlow = MutableSharedFlow<Effect>()
  val effect = effectsFlow.asSharedFlow()

  init {
    viewModelScope.launch {
      val field = fieldFlow.firstOrNull() ?: return@launch
      val block = blockFlow.firstOrNull() ?: return@launch

      if (!field.canPlaceBlockAny(block)) {
        dialogFlow.emit(true)
      }
    }
  }

  fun resetGame() {
    viewModelScope.launch {
      preferences.updateField { Field.Empty }
      preferences.updateScore { 0L }
      dialogFlow.emit(false)
    }
  }

  fun dragEnd(fieldBounds: IntRect, blockBounds: IntRect, cellSize: Float, spacing: Float) {
    viewModelScope.launch {
      val topLeft =
        findMatchingCellIndex(
          fieldBounds = fieldBounds,
          blockBounds = blockBounds,
          cellSize = cellSize,
          spacing = spacing,
        )

      when {
        topLeft == null -> Unit
        !field.value.canPlaceBlock(block = block.value, coordinates = topLeft) -> Unit
        else -> placeBlock(topLeft)
      }

      effectsFlow.emit(Effect.ResetPosition())
    }
  }

  private suspend fun placeBlock(topLeft: IntOffset) {
    preferences.updateField { current -> current.placeBlock(block = block.value, coordinates = topLeft) }

    preferences.updateScore { it + block.value.list.size }

    preferences.updateField { current -> current.clearFilledLines() }

    val nextBlock = Block.random()

    blockFlow.update { nextBlock }

    if (!field.value.canPlaceBlockAny(nextBlock)) {
      dialogFlow.emit(true)
    }
  }

  private fun findMatchingCellIndex(
    fieldBounds: IntRect,
    blockBounds: IntRect,
    cellSize: Float,
    spacing: Float,
  ): IntOffset? {
    if (!fieldBounds.contains(blockBounds.center)) return null

    val offset = blockBounds.topLeft - fieldBounds.topLeft
    val effectiveSize = cellSize + spacing

    val col = (offset.x / effectiveSize).roundToInt().coerceIn(0, field.value.dimension - 1)
    val row = (offset.y / effectiveSize).roundToInt().coerceIn(0, field.value.dimension - 1)

    return IntOffset(x = col, y = row)
  }

  companion object {

    val ApplicationKey = object : CreationExtras.Key<Application> {}

    val Factory: ViewModelProvider.Factory = viewModelFactory {
      initializer {
        val application = this[ApplicationKey] as Application
        FieldViewModel(application)
      }
    }
  }
}

sealed interface Effect {

  class ResetPosition : Effect
}
