package dev.vpestryakov.block_breaker.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import dev.vpestryakov.block_breaker.CrashReporter
import dev.zacsweers.metro.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Inject
class SettingsPreferences(
  private val dataStore: DataStore<Preferences>,
  private val crashReporter: CrashReporter,
) {

  private val data =
    dataStore.data.catch { throwable ->
      if (throwable is CancellationException) throw throwable
      crashReporter.recordException(throwable)
      emit(emptyPreferences())
    }

  val themeModeFlow =
    data.map { preferences ->
      ThemeMode.Saver.toOrDefault(preferences[ThemeKey], onFailure = crashReporter::recordException)
    }

  suspend fun updateTheme(themeMode: ThemeMode) {
    runCatching {
      dataStore.edit { preferences -> preferences[ThemeKey] = ThemeMode.Saver.from(themeMode) }
    }.onFailure { throwable ->
      if (throwable is CancellationException) throw throwable
      crashReporter.recordException(throwable)
    }
  }

  companion object {

    private val ThemeKey = intPreferencesKey("ThemeMode")
  }
}
