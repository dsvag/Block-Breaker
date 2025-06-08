package dev.vpestryakov.block_breaker

import dev.vpestryakov.block_breaker.utils.ThemeConfigurationOverrider
import dev.vpestryakov.block_breaker.utils.ThemeHolder
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

@GraphExtension
interface ActivityComponent {
  val themeHolder: ThemeHolder
  val themeConfigurationOverrider: ThemeConfigurationOverrider
  val metroViewModelFactory: MetroViewModelFactory
}