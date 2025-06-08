package dev.vpestryakov.block_breaker.ui.settings

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.vpestryakov.block_breaker.resource.Drawables
import dev.vpestryakov.block_breaker.ui.kit.Icon
import dev.vpestryakov.block_breaker.ui.theme.LocalColors
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

@Composable
fun Settings() {
  val viewModel = metroViewModel<SettingsViewModel>()

  var isExpanded by remember { mutableStateOf(false) }

  if (isExpanded) {
    LaunchedEffect(Unit) {
      delay(5.seconds)
      isExpanded = false
    }
  }

  Column(
    modifier = Modifier.padding(vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(space = 4.dp),
  ) {
    Button(
      resId = Drawables.ic_settings,
      onClick = { isExpanded = !isExpanded },
    )

    repeat(AnimationParts) { i ->
      val mode = ThemeMode.entries[i]
      val iconResId =
        when (mode) {
          ThemeMode.Light -> Drawables.ic_sun
          ThemeMode.Dark -> Drawables.ic_moon
          ThemeMode.System -> Drawables.ic_watch
        }

      AnimatedVisibility(
        visible = isExpanded,
        enter = slideInHorizontally(animationSpec = enterAnimationSpec(i), initialOffsetX = { it }),
        exit = slideOutHorizontally(animationSpec = exitAnimationSpec(i), targetOffsetX = { it }),
      ) {
        Button(
          resId = iconResId,
          onClick = {
            viewModel.updateMode(mode)
            isExpanded = false
          },
        )
      }
    }
  }
}

private const val AnimationLength = 300
private val AnimationParts = ThemeMode.entries.size
private const val AnimationPartTime = AnimationLength / 2
private val AnimationPartDelayTime = AnimationLength / AnimationParts

private fun <T> enterAnimationSpec(index: Int): TweenSpec<T> =
  tween(
    durationMillis = AnimationPartTime,
    delayMillis = AnimationPartDelayTime * index,
    easing = EaseInOut,
  )

private fun <T> exitAnimationSpec(index: Int): TweenSpec<T> =
  tween(
    durationMillis = AnimationPartTime,
    delayMillis = AnimationPartDelayTime * (AnimationParts - index),
    easing = EaseInOut,
  )

@Composable
private fun Button(
  @DrawableRes resId: Int,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  val colors = LocalColors.current

  Box(
    modifier =
      Modifier.padding(horizontal = 8.dp)
        .clickable(onClick = onClick)
        .size(size = 48.dp)
        .clip(shape = CircleShape)
        .drawBehind { drawRect(color = colors.button) }
        .then(other = modifier),
    contentAlignment = Alignment.Center,
    content = {
      Icon(
        modifier = Modifier.size(size = 24.dp),
        painter = painterResource(resId),
        tint = colors.onButtonText,
      )
    },
  )
}
