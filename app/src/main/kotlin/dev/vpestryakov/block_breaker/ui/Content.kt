package dev.vpestryakov.block_breaker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.vpestryakov.block_breaker.ui.field.Field
import dev.vpestryakov.block_breaker.ui.field.FieldViewModel
import dev.vpestryakov.block_breaker.ui.kit.blur
import dev.vpestryakov.block_breaker.ui.settings.Settings
import dev.vpestryakov.block_breaker.ui.theme.LocalColors
import dev.vpestryakov.block_breaker.ui.theme.Typography
import dev.vpestryakov.block_breaker.utils.NavigationBarsSpace
import dev.vpestryakov.block_breaker.utils.StatusBarsSpace
import dev.vpestryakov.block_breaker.utils.VerticalSpace
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun Content(onGameLoaded: () -> Unit = {}) {
  val colors = LocalColors.current

  val viewModel = metroViewModel<FieldViewModel>()

  val game = viewModel.game.collectAsStateWithLifecycle().value
  if (game != null) {
    SideEffect {
      onGameLoaded()
    }
  }
  val dialog = game?.dialog == true

  val blur = animateFloatAsState(if (dialog) 1F else 0F, tween())

  Column(
    modifier =
      Modifier
        .fillMaxSize()
        .drawBehind { drawRect(color = colors.background) }
        .blur(fraction = { blur.value }),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    content = {
      StatusBarsSpace()
      VerticalSpace(24.dp)
      if (game != null) {
        Field(gameState = game, viewModel = viewModel)
      }
      NavigationBarsSpace()
    },
  )

  AnimatedVisibility(
    visible = dialog,
    enter = fadeIn(tween()),
    exit = fadeOut(tween()),
    content = {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(key1 = Unit, block = { detectTapGestures {} }),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
          BasicText(text = "The end", style = Typography.HeadingS, softWrap = false, color = { colors.onButtonText })

          Spacer(modifier = Modifier.height(20.dp))

          Box(
            modifier =
              Modifier
                .clickable { viewModel.resetGame() }
                .size(width = 150.dp, height = 50.dp)
                .drawBehind {
                  val cornerPx = 10.dp.toPx()
                  drawRoundRect(
                    color = colors.button,
                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                  )
                },
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

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding(),
    contentAlignment = Alignment.TopEnd,
    content = { Settings() },
  )
}
