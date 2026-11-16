package dev.vpestryakov.block_breaker.ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import dev.vpestryakov.block_breaker.dto.Saver

enum class ThemeMode {
  Light,
  Dark,
  System;

  fun isDarkTheme(systemIsDark: Boolean): Boolean =
    when (this) {
      Dark -> true
      Light -> false
      System -> systemIsDark
    }

  @Composable
  fun isDarkTheme(): Boolean = isDarkTheme(isSystemInDarkTheme())

  companion object {

    val Saver =
      Saver(
        default = System,
        from = { it.ordinal },
        to = { ThemeMode.entries.getOrElse(it) { System } },
      )
  }
}
