package dev.vpestryakov.block_breaker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dev.vpestryakov.block_breaker.utils.dataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import kotlin.reflect.KClass

@DependencyGraph(AppScope::class)
interface AppComponent : ViewModelGraph {

  val activityComponent: ActivityComponent
  val androidCrashReporter: AndroidCrashReporter

  @Provides
  fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore

  @Provides
  fun provideMetroViewModelFactory(
    viewModelProviders: Map<KClass<out ViewModel>, () -> ViewModel>,
  ): MetroViewModelFactory =
    object : MetroViewModelFactory() {
      override val viewModelProviders = viewModelProviders
    }

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Provides context: Context): AppComponent
  }
}
