package dev.vpestryakov.block_breaker.ui.field

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import dev.vpestryakov.block_breaker.dto.Block
import dev.vpestryakov.block_breaker.dto.Game
import dev.vpestryakov.block_breaker.ui.theme.Colors
import dev.vpestryakov.block_breaker.ui.theme.LocalColors
import dev.vpestryakov.block_breaker.ui.theme.Typography
import dev.vpestryakov.block_breaker.utils.toDp
import dev.vpestryakov.block_breaker.utils.toPx
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private val Spacing = 4.dp
private val ContentPadding = Spacing * 2
private val Corner = 8.dp
private const val MinBoardToTrayHeightFactor = 1.4F

@Composable
fun ColumnScope.Field(gameState: Game, viewModel: FieldViewModel) {
  val colors = LocalColors.current
  val density = LocalDensity.current

  val dimension = gameState.field.dimension

  val fieldBounds = remember { mutableStateOf(IntRect.Zero) }

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    BasicText(
      text = gameState.topScore.toString(),
      style = Typography.HeadingS,
      softWrap = false,
      maxLines = 1,
      color = { colors.topScore },
    )
    BasicText(
      text = gameState.score.toString(),
      style = Typography.HeadingM,
      softWrap = false,
      maxLines = 1,
      color = { colors.score },
    )
  }

  val ghostPreview = remember { mutableStateOf<GhostPreview?>(null) }

  val activeBlockIndex = remember { mutableStateOf<Int?>(null) }
  val batchId = remember { IntArray(1) }
  val previousBlocks = remember { arrayOf(gameState.blocks) }
  var isAppearing = false
  if (previousBlocks[0] != gameState.blocks) {
    val curr = gameState.blocks
    val isNewBatch = curr.first != null && curr.second != null && curr.third != null
    if (isNewBatch) {
      batchId[0]++
      isAppearing = true
    }
    previousBlocks[0] = curr
  }

  BoxWithConstraints(
    modifier =
      Modifier
        .weight(weight = 1F)
        .fillMaxWidth(),
    contentAlignment = Alignment.TopCenter,
  ) {
    val boardSize = minOf(maxWidth, maxHeight / MinBoardToTrayHeightFactor)
    val widthDp = if (fieldBounds.value.width > 0) fieldBounds.value.width.toDp(density) else boardSize
    val cellSize =
      remember(widthDp, dimension) {
        if (widthDp > 0.dp && dimension > 0) {
          (widthDp - ContentPadding * 2 - Spacing * (dimension - 1)) / dimension
        } else {
          0.dp
        }
      }

    Column(
      modifier =
        Modifier
          .width(boardSize)
          .fillMaxHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      LazyVerticalGrid(
        modifier =
          Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 1F)
            .onLayoutRectChanged(
              throttleMillis = 0,
              debounceMillis = 0,
              callback = { fieldBounds.value = it.boundsInWindow },
            ),
        columns = GridCells.Fixed(dimension),
        verticalArrangement = Arrangement.spacedBy(Spacing),
        horizontalArrangement = Arrangement.spacedBy(Spacing),
        userScrollEnabled = false,
        contentPadding = PaddingValues(all = ContentPadding),
        content = {
          itemsIndexed(
            items = gameState.field,
            key = { index, _ -> index },
            contentType = { _, _ -> "cell" },
            itemContent = { index, cellColor ->
              val isFilled = cellColor != null
              val lastBlockColor = remember { mutableStateOf(cellColor) }
              if (cellColor != null) {
                lastBlockColor.value = cellColor
              }

              val animatedFractionState =
                animateFloatAsState(
                  targetValue = if (isFilled) 1F else 0F,
                  animationSpec =
                    when {
                      isFilled -> snap()
                      else -> tween(durationMillis = 500, easing = EaseOut)
                    },
                )

              val cellCol = index % dimension
              val cellRow = index / dimension

              Spacer(
                modifier =
                  Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 1F)
                    .drawBehind {
                      val cornerPx = Corner.toPx()
                      val preview = ghostPreview.value
                      val isGhostCell =
                        !isFilled &&
                          preview != null &&
                          preview.block.list.fastAny { offset ->
                            preview.topLeft.x + offset.x == cellCol && preview.topLeft.y + offset.y == cellRow
                          }

                      val cellDrawColor =
                        if (isGhostCell) {
                          lerp(colors.emptyCell, colors.blockColor(preview.block.color), GhostAlpha)
                        } else {
                          val fillFraction = if (isFilled) 1F else animatedFractionState.value
                          val activeColor = lastBlockColor.value?.let(colors::blockColor) ?: colors.filledCell
                          lerp(colors.emptyCell, activeColor, fillFraction)
                        }

                      drawRoundRect(
                        color = cellDrawColor,
                        cornerRadius = CornerRadius(cornerPx, cornerPx),
                      )
                    }
              )
            },
          )
        },
      )

      Row(
        modifier =
          Modifier
            .weight(weight = 1F)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        repeat(3) { blockIndex ->
          val block = gameState.blocks.get(blockIndex)
          Box(
            modifier =
              Modifier
                .weight(weight = 1F)
                .fillMaxHeight()
                .zIndex(if (activeBlockIndex.value == blockIndex) 10F else 0F),
            contentAlignment = Alignment.Center,
          ) {
            if (block != null && cellSize > 0.dp) {
              key(batchId[0], blockIndex, block) {
                DraggableBlock(
                  block = block,
                  blockIndex = blockIndex,
                  animateAppearance = isAppearing,
                  cellSize = cellSize,
                  colors = colors,
                  density = density,
                  fieldBounds = fieldBounds.value,
                  viewModel = viewModel,
                  onActiveChange = { active ->
                    if (active) {
                      activeBlockIndex.value = blockIndex
                    } else if (activeBlockIndex.value == blockIndex) {
                      activeBlockIndex.value = null
                      ghostPreview.value = null
                    }
                  },
                  onGhostChange = { targetCoordinates ->
                    val nextPreview = targetCoordinates?.let { GhostPreview(block = block, topLeft = it) }
                    if (ghostPreview.value != nextPreview) {
                      ghostPreview.value = nextPreview
                    }
                  },
                  findPlacementTarget = { dragBounds ->
                    viewModel.findPlacementTarget(
                      fieldBounds = fieldBounds.value,
                      blockBounds = dragBounds,
                      cellSize = cellSize.toPx(density),
                      spacing = Spacing.toPx(density),
                      contentPadding = ContentPadding.toPx(density),
                      blockIndex = blockIndex,
                    )
                  },
                  onCommitPlacement = { coordinates ->
                    viewModel.commitPlaceBlock(
                      blockIndex = blockIndex,
                      coordinates = coordinates,
                    )
                  },
                )
              }
            }
          }
        }
      }
    }
  }
}

private data class GhostPreview(
  val block: Block,
  val topLeft: IntOffset,
)

private const val GhostAlpha = 0.42F
private const val RestingBlockScale = 0.55F
private const val DraggingBlockScale = 1F
private val DragFingerClearance = 48.dp

@Composable
private fun DraggableBlock(
  block: Block,
  blockIndex: Int,
  animateAppearance: Boolean,
  cellSize: Dp,
  colors: Colors,
  density: Density,
  fieldBounds: IntRect,
  viewModel: FieldViewModel,
  onActiveChange: (Boolean) -> Unit,
  onGhostChange: (IntOffset?) -> Unit,
  findPlacementTarget: (IntRect) -> IntOffset?,
  onCommitPlacement: (IntOffset) -> Unit,
) {
  val scope = rememberCoroutineScope()
  val slotBounds = remember { mutableStateOf(IntRect.Zero) }
  val dragOffset = remember { mutableStateOf(Offset.Zero) }
  val returnAnim = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
  val shouldAnimateAppearance = remember { animateAppearance }
  val appearAnim = remember { Animatable(if (shouldAnimateAppearance) 0F else 1F) }
  val animJob = remember { mutableStateOf<Job?>(null) }
  val isDragging = remember { mutableStateOf(false) }
  val isSnappingToGrid = remember { mutableStateOf(false) }
  val currentFindPlacementTarget = rememberUpdatedState(findPlacementTarget)
  val currentOnCommitPlacement = rememberUpdatedState(onCommitPlacement)
  val currentOnActiveChange = rememberUpdatedState(onActiveChange)
  val currentOnGhostChange = rememberUpdatedState(onGhostChange)
  val currentFieldBounds = rememberUpdatedState(fieldBounds)

  if (shouldAnimateAppearance) {
    LaunchedEffect(Unit) {
      delay((blockIndex * 50L).milliseconds)
      appearAnim.animateTo(
        targetValue = 1F,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
      )
    }
  }

  val dragScale by animateFloatAsState(
    targetValue = if (isDragging.value || isSnappingToGrid.value) DraggingBlockScale else RestingBlockScale,
    animationSpec = spring(stiffness = Spring.StiffnessLow),
  )

  val blockWidthDp = cellSize * block.size.width + Spacing * block.spacingCount.x
  val blockHeightDp = cellSize * block.size.height + Spacing * block.spacingCount.y

  val fingerLiftTargetPx =
    remember(blockHeightDp, density) {
      -(blockHeightDp / 2F + DragFingerClearance).toPx(density)
    }

  val fingerLiftOffsetY by animateFloatAsState(
    targetValue = if (isDragging.value) fingerLiftTargetPx else 0F,
    animationSpec = spring(stiffness = Spring.StiffnessLow),
  )

  val computeBlockBounds = { visualOffset: Offset ->
    val blockWidthPx = blockWidthDp.toPx(density)
    val blockHeightPx = blockHeightDp.toPx(density)
    val center =
      IntOffset(
        x = slotBounds.value.center.x + visualOffset.x.roundToInt(),
        y = slotBounds.value.center.y + visualOffset.y.roundToInt(),
      )
    IntRect(
      offset = IntOffset((center.x - blockWidthPx / 2F).roundToInt(), (center.y - blockHeightPx / 2F).roundToInt()),
      size = IntSize(blockWidthPx.roundToInt(), blockHeightPx.roundToInt()),
    )
  }

  LaunchedEffect(viewModel) {
    viewModel.effects.collect { effect ->
      when (effect) {
        is Effect.ResetPosition -> {
          animJob.value?.cancel()
          isDragging.value = false
          isSnappingToGrid.value = false
          currentOnGhostChange.value.invoke(null)
          currentOnActiveChange.value.invoke(false)
          dragOffset.value = Offset.Zero
        }
      }
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      currentOnGhostChange.value.invoke(null)
      currentOnActiveChange.value.invoke(false)
    }
  }

  val finishDrag = {
    if (isDragging.value) {
      val startOffset = Offset(x = dragOffset.value.x, y = dragOffset.value.y + fingerLiftOffsetY)
      dragOffset.value = startOffset
      isDragging.value = false
      currentOnGhostChange.value.invoke(null)

      val blockWidthPx = blockWidthDp.toPx(density)
      val blockHeightPx = blockHeightDp.toPx(density)
      val currentBlockBounds = computeBlockBounds(startOffset)

      val targetCoordinates = currentFindPlacementTarget.value.invoke(currentBlockBounds)
      animJob.value?.cancel()

      if (targetCoordinates != null) {
        isSnappingToGrid.value = true
        val cellSizePx = cellSize.toPx(density)
        val spacingPx = Spacing.toPx(density)
        val contentPaddingPx = ContentPadding.toPx(density)
        val effectiveSize = cellSizePx + spacingPx
        val fieldRect = currentFieldBounds.value

        val targetTopLeftX = fieldRect.left + contentPaddingPx + targetCoordinates.x * effectiveSize
        val targetTopLeftY = fieldRect.top + contentPaddingPx + targetCoordinates.y * effectiveSize
        val restingTopLeftX = slotBounds.value.center.x - blockWidthPx / 2F
        val restingTopLeftY = slotBounds.value.center.y - blockHeightPx / 2F

        val targetOffset =
          Offset(
            x = targetTopLeftX - restingTopLeftX,
            y = targetTopLeftY - restingTopLeftY,
          )

        animJob.value =
          scope.launch {
            returnAnim.snapTo(startOffset)
            returnAnim.animateTo(
              targetValue = targetOffset,
              animationSpec = tween(durationMillis = 120, easing = EaseOut),
            ) {
              dragOffset.value = value
            }
            currentOnCommitPlacement.value.invoke(targetCoordinates)
          }
      } else {
        animJob.value =
          scope.launch {
            returnAnim.snapTo(startOffset)
            returnAnim.animateTo(
              targetValue = Offset.Zero,
              animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            ) {
              dragOffset.value = value
            }
            if (!isDragging.value) {
              currentOnActiveChange.value.invoke(false)
            }
          }
      }
    }
  }

  Box(
    modifier =
      Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .onLayoutRectChanged(
          throttleMillis = 0,
          debounceMillis = 0,
          callback = { slotBounds.value = it.boundsInWindow },
        )
        .pointerInput(block, cellSize) {
          detectDragGestures(
            onDragStart = {
              if (isSnappingToGrid.value) return@detectDragGestures
              animJob.value?.cancel()
              isDragging.value = true
              currentOnActiveChange.value.invoke(true)
            },
            onDragEnd = { finishDrag() },
            onDragCancel = { finishDrag() },
            onDrag = { change, dragAmount ->
              if (!isDragging.value) return@detectDragGestures
              change.consume()
              val nextDragOffset = dragOffset.value + dragAmount
              dragOffset.value = nextDragOffset
              val visualOffset = Offset(x = nextDragOffset.x, y = nextDragOffset.y + fingerLiftOffsetY)
              val target = currentFindPlacementTarget.value.invoke(computeBlockBounds(visualOffset))
              currentOnGhostChange.value.invoke(target)
            },
          )
        },
    contentAlignment = Alignment.Center,
  ) {
    Spacer(
      modifier =
        Modifier
          .requiredSize(width = blockWidthDp, height = blockHeightDp)
          .graphicsLayer {
            val currentScale = dragScale * appearAnim.value
            val liftY = if (isDragging.value) fingerLiftOffsetY else 0F
            translationX = dragOffset.value.x
            translationY = dragOffset.value.y + liftY
            scaleX = currentScale
            scaleY = currentScale
            alpha = appearAnim.value.fastCoerceIn(0F, 1F)
          }
          .drawBehind {
            val blockColor = colors.blockColor(block.color)
            val pxSize = cellSize.toPx()
            val spacingPx = Spacing.toPx()
            val cornerPx = Corner.toPx()

            block.list.fastForEach { offset ->
              drawRoundRect(
                color = blockColor,
                topLeft = Offset(x = offset.x * (pxSize + spacingPx), y = offset.y * (pxSize + spacingPx)),
                size = Size(pxSize, pxSize),
                cornerRadius = CornerRadius(cornerPx, cornerPx),
              )
            }
          },
    )
  }
}
