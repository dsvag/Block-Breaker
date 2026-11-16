package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import dev.vpestryakov.block_breaker.dto.BlockColor
import kotlin.reflect.KProperty

val LocalColors = compositionLocalOf<Colors> { error("No color palette provided") }

@Stable
class Colors(
  private val darkFraction: State<Float>,
) {
  val background by color(light = Color(0xFFFFFEE0), dark = Color(0xFF000000))
  val emptyCell by color(light = Color(0xFFCFCFC4), dark = Color(0xFF1A1A1A))
  val filledCell by color(light = Color(0xFFF1BEB5), dark = Color(0xFFF1BEB5))
  val topScore by color(light = Color(0xFFE7D27C), dark = Color(0xFFF8C57C))
  val score by color(light = Color(0xFFA4D8D8), dark = Color(0xFFA4D8D8))
  val button by color(light = Color(0xFFD1FEB8), dark = Color(0xFFE19083))
  val onButtonText by color(light = Color(0xFFFFFEE0), dark = Color(0xFFFFFEE0))

  private val coralBlock by color(light = Color(0xFFE06D53), dark = Color(0xFFF4978E))
  private val amberBlock by color(light = Color(0xFFE89923), dark = Color(0xFFF8C57C))
  private val mintBlock by color(light = Color(0xFF43AA8B), dark = Color(0xFFA8E6CF))
  private val tealBlock by color(light = Color(0xFF2D9CDB), dark = Color(0xFF88D4DC))
  private val skyBlock by color(light = Color(0xFF4A6FA5), dark = Color(0xFF98B9F2))
  private val lavenderBlock by color(light = Color(0xFF8E6CEF), dark = Color(0xFFC8B6FF))
  private val roseBlock by color(light = Color(0xFFD95D82), dark = Color(0xFFF1BEB5))

  fun blockColor(blockColor: BlockColor): Color =
    when (blockColor) {
      BlockColor.Coral -> coralBlock
      BlockColor.Amber -> amberBlock
      BlockColor.Mint -> mintBlock
      BlockColor.Teal -> tealBlock
      BlockColor.Sky -> skyBlock
      BlockColor.Lavender -> lavenderBlock
      BlockColor.Rose -> roseBlock
    }

  private fun color(light: Color, dark: Color): ThemableColor = ThemableColor(light, dark)

  private inner class ThemableColor(
    private val light: Color,
    private val dark: Color,
  ) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Color =
      if (light == dark) light else lerp(light, dark, darkFraction.value)
  }
}

