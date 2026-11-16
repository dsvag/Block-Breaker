package dev.vpestryakov.block_breaker.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import dev.vpestryakov.block_breaker.shared.R

private val FontProvider =
  GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.google_fonts_certs,
  )

private val PacificoFont = GoogleFont(name = "Pacifico")

internal actual val PacificoFontFamily: FontFamily =
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
