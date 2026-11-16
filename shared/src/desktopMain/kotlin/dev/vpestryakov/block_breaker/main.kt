package dev.vpestryakov.block_breaker

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.vpestryakov.block_breaker.ui.BlockBreakerApp
import dev.vpestryakov.block_breaker.utils.platformDataStore
import dev.zacsweers.metro.createGraphFactory

private val appComponent: SharedAppComponent by lazy {
  createGraphFactory<SharedAppComponent.Factory>().create(platformDataStore)
}

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    title = "Block Breaker",
    state = rememberWindowState(size = DpSize(width = 450.dp, height = 800.dp)),
    resizable = false,
  ) {
    BlockBreakerApp(component = appComponent)
  }
}
