package dev.vpestryakov.block_breaker.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

val platformDataStore: DataStore<Preferences> by lazy {
  createDataStore(
    producePath = {
      val dir = File(System.getProperty("user.home"), ".block_breaker")
      dir.mkdirs()
      File(dir, DataStoreFileName).absolutePath
    },
  )
}
