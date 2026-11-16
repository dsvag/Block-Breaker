package dev.vpestryakov.block_breaker

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.vpestryakov.block_breaker.dto.Blocks
import dev.vpestryakov.block_breaker.dto.Field
import dev.vpestryakov.block_breaker.dto.Game
import dev.zacsweers.metro.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Inject
class GamePreferences(
  private val dataStore: DataStore<Preferences>,
  private val crashReporter: CrashReporter,
) {

  private val defaultBlocks by lazy { Blocks.random() }

  private fun parseGame(preferences: Preferences): Game {
    val score = preferences[ScoreKey] ?: 0L
    val topScore = maxOf(preferences[TopScoreKey] ?: 0L, score)
    val field =
      preferences[FieldKey]
        ?.let { raw ->
          runCatching { Field.Saver.to(raw) }
            .onFailure {
              crashReporter.log("Failed to parse saved field")
              crashReporter.recordException(it)
            }
            .getOrNull()
        } ?: Field.Empty
    val blocks =
      preferences[BlocksKey]
        ?.let { raw ->
          runCatching { Blocks.Saver.to(raw) }
            .onFailure {
              crashReporter.log("Failed to parse saved blocks")
              crashReporter.recordException(it)
            }
            .getOrNull()
        } ?: defaultBlocks
    return Game(
      field = field,
      blocks = blocks,
      score = score,
      topScore = topScore,
    )
  }

  val gameFlow =
    dataStore.data
      .catch { throwable ->
        if (throwable is CancellationException) throw throwable
        crashReporter.recordException(throwable)
        emit(emptyPreferences())
      }
      .map(::parseGame)

  suspend fun updateGame(function: (Game) -> Game) {
    runCatching {
      dataStore.edit { preferences ->
        val current = parseGame(preferences)
        val updated = function(current)
        val updatedTopScore = maxOf(current.topScore, updated.score, updated.topScore)
        preferences[FieldKey] = Field.Saver.from(updated.field)
        preferences[BlocksKey] = Blocks.Saver.from(updated.blocks)
        preferences[ScoreKey] = updated.score
        preferences[TopScoreKey] = updatedTopScore
      }
    }.onFailure { throwable ->
      if (throwable is CancellationException) throw throwable
      crashReporter.recordException(throwable)
    }
  }

  suspend fun reset() {
    runCatching {
      dataStore.edit { preferences ->
        preferences[FieldKey] = Field.Saver.from(Field.Empty)
        preferences[ScoreKey] = 0L
        preferences[BlocksKey] = Blocks.Saver.from(Blocks.random())
      }
    }.onFailure { throwable ->
      if (throwable is CancellationException) throw throwable
      crashReporter.recordException(throwable)
    }
  }

  companion object {

    private val BlocksKey = stringPreferencesKey("Blocks")
    private val FieldKey = stringPreferencesKey("Field")
    private val ScoreKey = longPreferencesKey("Score")
    private val TopScoreKey = longPreferencesKey("TopScore")
  }
}
