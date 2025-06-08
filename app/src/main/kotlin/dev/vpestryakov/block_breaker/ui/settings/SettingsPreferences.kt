package dev.vpestryakov.block_breaker.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.map

@Inject
class SettingsPreferences(
  private val dataStore: DataStore<Preferences>
) {

  private val data = dataStore.data

  val themeModeFlow = data.map { preferences -> ThemeMode.Saver.toOrDefault(preferences[ThemeKey]) }

  suspend fun updateTheme(themeMode: ThemeMode) {
    dataStore.edit { preferences -> preferences[ThemeKey] = ThemeMode.Saver.from(themeMode) }
  }

  companion object {

    private val ThemeKey = intPreferencesKey("ThemeMode")
  }
}
