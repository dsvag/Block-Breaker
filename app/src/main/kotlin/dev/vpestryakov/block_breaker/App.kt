package dev.vpestryakov.block_breaker

import android.app.Application
import dev.zacsweers.metro.createGraphFactory

class App : Application() {

  lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()
    appComponent = createGraphFactory<AppComponent.Factory>().create(this)
  }
}
