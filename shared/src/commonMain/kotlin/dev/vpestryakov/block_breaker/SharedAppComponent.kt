package dev.vpestryakov.block_breaker

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dev.vpestryakov.block_breaker.ui.settings.SettingsPreferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import kotlin.reflect.KClass

@DependencyGraph(AppScope::class)
interface SharedAppComponent : ViewModelGraph {

  val settingsPreferences: SettingsPreferences

  @Provides
  fun provideMetroViewModelFactory(
    viewModelProviders: Map<KClass<out ViewModel>, () -> ViewModel>,
  ): MetroViewModelFactory =
    object : MetroViewModelFactory() {
      override val viewModelProviders = viewModelProviders
    }

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Provides dataStore: DataStore<Preferences>): SharedAppComponent
  }
}
