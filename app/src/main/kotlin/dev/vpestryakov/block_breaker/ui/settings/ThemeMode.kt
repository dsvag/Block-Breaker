package dev.vpestryakov.block_breaker.ui.settings

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import dev.vpestryakov.block_breaker.dto.Saver

enum class ThemeMode {
  Light,
  Dark,
  System;

  fun isDarkTheme(context: Context): Boolean =
    when (this) {
      Dark -> true
      Light -> false
      System -> context.isSystemInDarkTheme()
    }

  companion object {

    val Saver =
      Saver(
        default = System,
        from = { it.ordinal },
        to = { ThemeMode.entries.getOrElse(it) { System } },
      )
  }
}

private fun Context.isSystemInDarkTheme(): Boolean {
  val uiMode = resources.configuration.uiMode
  return uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}
