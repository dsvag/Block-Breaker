package dev.vpestryakov.block_breaker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.vpestryakov.block_breaker.ui.theme.LocalColors
import dev.vpestryakov.block_breaker.ui.theme.Typography

private val Spacing = 4.dp
private val Corner = 8.dp

@Composable
fun Content(extras: CreationExtras) {
  val viewModel = viewModel<FieldViewModel>(factory = FieldViewModel.Factory, extras = extras)

  val dialog by viewModel.dialog.collectAsStateWithLifecycle()

  val colors = LocalColors.current
  val backgroundColor = colors.background

  Column(
    modifier = Modifier.fillMaxSize().background(color = backgroundColor).statusBarsPadding().navigationBarsPadding(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    content = { Field(viewModel) },
  )

  AnimatedVisibility(
    visible = dialog,
    enter = fadeIn(),
    exit = fadeOut(),
    content = {
      Column(
        modifier =
          Modifier.fillMaxSize()
            .background(color = backgroundColor.copy(0.5F))
            .pointerInput(key1 = Unit, block = { detectTapGestures {} }),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
          BasicText(
            text = "The end",
            style = dev.vpestryakov.block_breaker.ui.theme.Typography.HeadingS,
            softWrap = false,
            color = { colors.onButtonText },
          )

          Spacer(modifier = Modifier.height(20.dp))

          Box(
            modifier =
              Modifier.clickable { viewModel.resetGame() }
                .size(width = 150.dp, height = 50.dp)
                .background(color = colors.button, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
            content = {
              BasicText(
                text = "Try Again",
                style = Typography.BodyBold,
                softWrap = false,
                maxLines = 1,
                color = { colors.onButtonText },
              )
            },
          )
        },
      )
    },
  )
}

@Composable
private fun ColumnScope.Field(viewModel: FieldViewModel) {
  val colors = LocalColors.current
  val density = LocalDensity.current

  val fieldState by viewModel.field.collectAsStateWithLifecycle()
  val blockState by viewModel.block.collectAsStateWithLifecycle()
  val score by viewModel.score.collectAsStateWithLifecycle()

  val cellSize = remember { mutableStateOf(0.dp) }
  val fieldBounds = remember { mutableStateOf(IntRect.Zero) }
  val dragBounds = remember { mutableStateOf(IntRect.Zero) }
  val offset = remember { mutableStateOf(Offset.Zero) }

  val onDragEnd = rememberUpdatedState {
    viewModel.dragEnd(
      fieldBounds = fieldBounds.value,
      blockBounds = dragBounds.value,
      cellSize = with(density) { cellSize.value.toPx() },
      spacing = with(density) { Spacing.toPx() },
    )
  }

  LaunchedEffect(viewModel) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is Effect.ResetPosition -> offset.value = Offset.Zero
      }
    }
  }

  Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center,
    content = {
      BasicText(
        text = score.toString(),
        style = Typography.HeadingM,
        softWrap = false,
        maxLines = 1,
        color = { colors.score },
      )
    },
  )

  LazyVerticalGrid(
    modifier =
      Modifier.fillMaxWidth()
        .aspectRatio(ratio = 1F)
        .onLayoutRectChanged(
          throttleMillis = 0,
          debounceMillis = 0,
          callback = { fieldBounds.value = it.boundsInWindow },
        ),
    columns = GridCells.Fixed(fieldState.dimension),
    verticalArrangement = Arrangement.spacedBy(Spacing),
    horizontalArrangement = Arrangement.spacedBy(Spacing),
    userScrollEnabled = false,
    contentPadding = PaddingValues(all = Spacing * 2),
    content = {
      itemsIndexed(
        items = fieldState,
        key = { index, isFilled -> index },
        itemContent = { index, isFilled ->
          val animatedColor by
            animateColorAsState(
              targetValue =
                when {
                  isFilled -> colors.filledCell
                  else -> colors.emptyCell
                },
              animationSpec =
                when {
                  isFilled -> snap()
                  else -> tween(durationMillis = 500, easing = EaseOut)
                },
            )

          Box(
            modifier =
              Modifier.fillMaxWidth()
                .aspectRatio(ratio = 1F)
                .graphicsLayer { cellSize.value = size.width.toDp() }
                .background(color = animatedColor, shape = RoundedCornerShape(Corner))
          )
        },
      )
    },
  )

  Box(modifier = Modifier.weight(weight = 1F).fillMaxWidth(), contentAlignment = Alignment.Center) {
    Canvas(
      modifier =
        Modifier.padding(all = Spacing)
          .size(
            width = cellSize.value * blockState.size.width + Spacing * blockState.spacingCount.first,
            height = cellSize.value * blockState.size.height + Spacing * blockState.spacingCount.second,
          )
          .graphicsLayer {
            translationX = offset.value.x
            translationY = offset.value.y
          }
          .zIndex(10F)
          .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
              onDragEnd = { onDragEnd.value.invoke() },
              onDragCancel = { onDragEnd.value.invoke() },
              onDrag = { _, dragAmount -> offset.value += dragAmount },
            )
          }
          .onLayoutRectChanged(
            throttleMillis = 0,
            debounceMillis = 0,
            callback = { dragBounds.value = it.boundsInWindow },
          ),
      onDraw = {
        val block = blockState
        val pxSize = cellSize.value.toPx()
        val spacingPx = Spacing.toPx()
        val cornerPx = Corner.toPx()

        block.list.fastForEach { (col, row) ->
          drawRoundRect(
            color = colors.filledCell,
            topLeft = Offset(x = col * (pxSize + spacingPx), y = row * (pxSize + spacingPx)),
            size = Size(pxSize, pxSize),
            cornerRadius = CornerRadius(cornerPx, cornerPx),
          )
        }
      },
    )
  }
}
