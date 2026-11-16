package dev.vpestryakov.block_breaker.utils

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.vpestryakov.block_breaker.ui.theme.LocalColors

@Composable
actual fun EnableEdge2Edge(isDarkThemeStatusBar: Boolean, isDarkThemeNavBar: Boolean) {
  val activity = LocalActivity.current as? ComponentActivity ?: return
  val colors = LocalColors.current

  SideEffect {
    val transparentColor = Color.Transparent.toArgb()
    val statusBarScrim = transparentColor
    val navBarScrim = if (isNavigationBarContrastSupported()) transparentColor else colors.background.toArgb()

    activity.enableEdgeToEdge(
      statusBarStyle =
        SystemBarStyle.auto(
          lightScrim = statusBarScrim,
          darkScrim = statusBarScrim,
          detectDarkMode = { isDarkThemeStatusBar },
        ),
      navigationBarStyle =
        SystemBarStyle.auto(
          lightScrim = navBarScrim,
          darkScrim = navBarScrim,
          detectDarkMode = { isDarkThemeNavBar },
        ),
    )
  }
}

private fun isNavigationBarContrastSupported(): Boolean {
  return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && DeviceManufacturerProvider.isXiaomi().not()
}

private object DeviceManufacturerProvider {

  private fun isManufacturer(manufacturer: String): Boolean {
    return Build.MANUFACTURER.equals(manufacturer, ignoreCase = true)
  }

  private const val XiaomiManufacturerName = "xiaomi"

  fun isXiaomi(): Boolean = isManufacturer(XiaomiManufacturerName)
}
