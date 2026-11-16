package dev.vpestryakov.block_breaker.utils

import androidx.compose.runtime.Composable

@Composable
fun EnableEdge2Edge(isDarkTheme: Boolean) {
  EnableEdge2Edge(
    isDarkThemeStatusBar = isDarkTheme,
    isDarkThemeNavBar = isDarkTheme,
  )
}

@Composable
expect fun EnableEdge2Edge(isDarkThemeStatusBar: Boolean, isDarkThemeNavBar: Boolean)
