package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import dev.vpestryakov.block_breaker.utils.EnableEdge2Edge

@Composable
fun BlockBreakerTheme(isDark: Boolean, content: @Composable () -> Unit) {
  val darkFraction = animateFloatAsState(
    targetValue = if (isDark) 1F else 0F,
    animationSpec = tween(durationMillis = 350)
  )

  val colors = remember { Colors(darkFraction) }

  val currentViewConfiguration = LocalViewConfiguration.current
  val viewConfiguration = remember(currentViewConfiguration) { currentViewConfiguration.updateViewConfiguration() }

  CompositionLocalProvider(
    LocalColors provides colors,
    LocalIndication provides ScaleIndicationNodeFactory,
    LocalViewConfiguration provides viewConfiguration,
    content = {
      EnableEdge2Edge(isDark)
      content()
    },
  )
}

private fun ViewConfiguration.updateViewConfiguration(): ViewConfiguration =
  object : ViewConfiguration by this {

    override val longPressTimeoutMillis = 100L
  }

