package dev.vpestryakov.block_breaker.ui.kit

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun Icon(painter: Painter, tint: Color, modifier: Modifier = Modifier) {
  val colorFilter = remember(tint) { tint.takeIf { it.isSpecified }?.let(ColorFilter::tint) }

  Box(
    modifier =
      modifier.paint(
        painter = painter,
        sizeToIntrinsics = false,
        colorFilter = colorFilter,
        contentScale = ContentScale.Fit,
      )
  )
}
