package dev.vpestryakov.block_breaker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class SettingsViewModel(
  private val preferences: SettingsPreferences
) : ViewModel() {

  fun updateMode(themeMode: ThemeMode) {
    viewModelScope.launch { preferences.updateTheme(themeMode) }
  }
}
