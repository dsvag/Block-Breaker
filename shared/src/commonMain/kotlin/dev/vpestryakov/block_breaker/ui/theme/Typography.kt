package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

internal expect val PacificoFontFamily: FontFamily

object Typography {
  val HeadingM =
    TextStyle(
      fontFamily = PacificoFontFamily,
      fontWeight = FontWeight.W700,
      fontSize = 60.sp,
      lineHeight = 1.2.em,
      lineHeightStyle = LineHeightStyleWithoutTrim,
    )
  val HeadingS =
    TextStyle(
      fontFamily = PacificoFontFamily,
      fontWeight = FontWeight.W700,
      fontSize = 40.sp,
      lineHeight = 1.2.em,
      lineHeightStyle = LineHeightStyleWithoutTrim,
    )
  val BodyBold =
    TextStyle(
      fontFamily = PacificoFontFamily,
      fontWeight = FontWeight.W700,
      fontSize = 16.sp,
      lineHeight = 1.2.em,
      lineHeightStyle = LineHeightStyleWithoutTrim,
    )
}

private val LineHeightStyleWithoutTrim =
  LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Proportional,
    trim = LineHeightStyle.Trim.None,
    mode = LineHeightStyle.Mode.Fixed,
  )
