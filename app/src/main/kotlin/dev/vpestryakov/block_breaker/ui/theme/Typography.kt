package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import dev.vpestryakov.block_breaker.R

private val FontProvider =
  GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.google_fonts_certs,
  )

private val PacificoFont = GoogleFont(name = "Pacifico")

private val PacificoFontFamily =
  FontFamily(
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W900),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W800),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W700),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W600),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W500),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W400),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W300),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W200),
    Font(googleFont = PacificoFont, fontProvider = FontProvider, weight = FontWeight.W100),
  )

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
