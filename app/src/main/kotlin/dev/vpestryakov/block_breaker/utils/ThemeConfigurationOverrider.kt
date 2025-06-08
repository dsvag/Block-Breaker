package dev.vpestryakov.block_breaker.utils

import android.content.Context
import android.content.res.Configuration
import dev.vpestryakov.block_breaker.ui.settings.SettingsPreferences
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@Inject
class ThemeConfigurationOverrider(
  private val preferences: SettingsPreferences,
) {

  fun overrideContext(context: Context): Context {
    val config = Configuration(context.resources.configuration)
    val isDark = runBlocking { preferences.themeModeFlow.first() }.isDarkTheme(context)
    config.setNightMode(isDark)
    return context.createConfigurationContext(config)
  }

  private fun Configuration.setNightMode(isNight: Boolean) {
    val notUiNightMask = Configuration.UI_MODE_NIGHT_MASK.inv()
    val uiModeWithoutNight = uiMode and notUiNightMask
    val flag = if (isNight) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
    uiMode = uiModeWithoutNight or flag
  }
}

@Inject
class ThemeHolder(
  private val context: Context,
  preferences: SettingsPreferences,
) {

  val isDarkThemeFlow =
    preferences.themeModeFlow.map { themeMode -> themeMode.isDarkTheme(context) }
}
