package dev.vpestryakov.block_breaker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.vpestryakov.block_breaker.SharedAppComponent
import dev.vpestryakov.block_breaker.ui.settings.ThemeMode
import dev.vpestryakov.block_breaker.ui.theme.BlockBreakerTheme
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

@Composable
fun BlockBreakerApp(
  component: SharedAppComponent,
  onGameLoaded: () -> Unit = {},
) {
  val themeMode by component.settingsPreferences.themeModeFlow.collectAsState(initial = ThemeMode.System)
  val isDarkTheme = themeMode.isDarkTheme()

  CompositionLocalProvider(
    LocalMetroViewModelFactory provides component.metroViewModelFactory,
  ) {
    BlockBreakerTheme(isDarkTheme) {
      Content(onGameLoaded = onGameLoaded)
    }
  }
}
