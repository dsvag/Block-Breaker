package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColors = compositionLocalOf<Colors> { error("No color palette provided") }

data class Colors(
  val background: Color,
  val emptyCell: Color,
  val filledCell: Color,
  val score: Color,
  val button: Color,
  val onButtonText: Color,
) {

  companion object {
    val Dark =
      Colors(
        background = Color(0xFF000000),
        emptyCell = Color(0xFF1A1A1A),
        filledCell = Color(0xFFF1BEB5),
        score = Color(0xFFA4D8D8),
        button = Color(0xFFE19083),
        onButtonText = Color(0xFFFFFEE0)
      )

    val Light =
      Colors(
        background = Color(0xFFFFFEE0),
        emptyCell = Color(0xFFCFCFC4),
        filledCell = Color(0xFFF1BEB5),
        score = Color(0xFFA4D8D8),
        button = Color(0xFFD1FEB8),
        onButtonText = Color(0xFFFFFEE0)
      )
  }
}

val a =
  listOf(
    Color(0xFFA4D8D8),
    Color(0xFFD1FEB8),
    Color(0xFFE7D27C),
    Color(0xFFEBCCFF),
    Color(0xFFF0EBD8),
    Color(0xFFF1BEB5),
    Color(0xFFF8C57C),
  )
