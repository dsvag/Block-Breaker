package dev.vpestryakov.block_breaker

import androidx.compose.ui.window.ComposeUIViewController
import dev.vpestryakov.block_breaker.ui.BlockBreakerApp
import dev.vpestryakov.block_breaker.utils.platformDataStore
import dev.zacsweers.metro.createGraphFactory
import platform.UIKit.UIViewController

private val appComponent: SharedAppComponent by lazy {
  createGraphFactory<SharedAppComponent.Factory>().create(platformDataStore)
}

fun MainViewController(): UIViewController =
  ComposeUIViewController {
    BlockBreakerApp(component = appComponent)
  }
