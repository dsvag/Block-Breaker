package dev.vpestryakov.block_breaker

import androidx.compose.ui.window.Window
import dev.vpestryakov.block_breaker.ui.BlockBreakerApp
import dev.vpestryakov.block_breaker.utils.platformDataStore
import dev.zacsweers.metro.createGraphFactory
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AppKit.NSApplication
import platform.AppKit.NSApplicationActivationPolicy
import platform.AppKit.NSWindow
import platform.AppKit.NSWindowStyleMaskResizable
import platform.Foundation.NSMakeSize

private val appComponent: SharedAppComponent by lazy {
  createGraphFactory<SharedAppComponent.Factory>().create(platformDataStore)
}

@OptIn(ExperimentalForeignApi::class)
fun main() {
  val app = NSApplication.sharedApplication()
  app.setActivationPolicy(
    NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular
  )
  Window(
    title = "Block Breaker",
  ) {
    BlockBreakerApp(component = appComponent)
  }
  (app.windows.firstOrNull() as? NSWindow)?.apply {
    styleMask = styleMask and NSWindowStyleMaskResizable.inv()
    setContentSize(NSMakeSize(450.0, 800.0))
    center()
  }
  app.run()
}
