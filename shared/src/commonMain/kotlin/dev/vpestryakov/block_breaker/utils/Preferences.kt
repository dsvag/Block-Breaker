package dev.vpestryakov.block_breaker.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

const val DataStoreFileName = "Preferences.preferences_pb"

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
  PreferenceDataStoreFactory.createWithPath(
    produceFile = { producePath().toPath() },
  )
