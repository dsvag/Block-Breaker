package dev.vpestryakov.block_breaker

import androidx.compose.ui.window.ComposeUIViewController
import dev.vpestryakov.block_breaker.ui.BlockBreakerApp
import dev.vpestryakov.block_breaker.utils.platformDataStore
import dev.zacsweers.metro.createGraphFactory
import kotlin.concurrent.Volatile
import platform.UIKit.UIViewController

private object IosCrashReporterDelegate : CrashReporter {
  @Volatile
  var delegate: CrashReporter = NoOpCrashReporter

  override fun recordException(throwable: Throwable) {
    delegate.recordException(throwable)
  }

  override fun log(message: String) {
    delegate.log(message)
  }

  override fun setCustomKey(key: String, value: String) {
    delegate.setCustomKey(key, value)
  }
}

fun registerCrashReporter(crashReporter: CrashReporter) {
  IosCrashReporterDelegate.delegate = crashReporter
}

private val appComponent: SharedAppComponent by lazy {
  createGraphFactory<SharedAppComponent.Factory>().create(
    dataStore = platformDataStore,
    crashReporter = IosCrashReporterDelegate,
  )
}

fun MainViewController(): UIViewController =
  ComposeUIViewController {
    BlockBreakerApp(component = appComponent)
  }
