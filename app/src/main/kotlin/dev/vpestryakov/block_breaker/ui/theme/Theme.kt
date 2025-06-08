package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration

@Composable
fun BlockBreakerTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
  val colors =
    when {
      darkTheme -> Colors.Dark
      else -> Colors.Light
    }

  val viewConfiguration = LocalViewConfiguration.current.updateViewConfiguration()

  CompositionLocalProvider(
    LocalColors provides colors,
    LocalIndication provides ScaleIndicationNodeFactory,
    LocalViewConfiguration provides viewConfiguration,
    content = content,
  )
}

private fun ViewConfiguration.updateViewConfiguration(): ViewConfiguration =
  object : ViewConfiguration by this {
    override val longPressTimeoutMillis = 100L
  }
