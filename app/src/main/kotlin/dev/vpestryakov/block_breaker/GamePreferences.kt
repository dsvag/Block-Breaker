package dev.vpestryakov.block_breaker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.vpestryakov.block_breaker.dto.Field
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "store")

class GamePreferences(private val dataStore: DataStore<Preferences>) {

  private val data = dataStore.data

  val fieldFlow = data.map { preferences -> preferences[FieldKey]?.let(Field.Saver.to) }.filterNotNull()

  val scoreFlow = data.map { preferences -> preferences[ScoreKey] }.filterNotNull()

  suspend fun updateField(function: (Field) -> Field) {
    dataStore.edit { preferences ->
      val current = preferences[FieldKey]?.let(Field.Saver.to) ?: Field.Empty
      val updated = function(current)
      preferences[FieldKey] = Field.Saver.from(updated)
    }
  }

  suspend fun updateScore(function: (Long) -> Long) {
    dataStore.edit { preferences ->
      val current = preferences[ScoreKey] ?: 0L
      preferences[ScoreKey] = function(current)
    }
  }

  companion object {

    private val ScoreKey = longPreferencesKey("Score")

    private val FieldKey = stringPreferencesKey("Field")
  }
}
