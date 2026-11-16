package dev.vpestryakov.block_breaker.ui.settings

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES

fun ThemeMode.isDarkTheme(context: Context): Boolean =
  isDarkTheme(context.isSystemInDarkTheme())

private fun Context.isSystemInDarkTheme(): Boolean {
  val uiMode = resources.configuration.uiMode
  return uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}
