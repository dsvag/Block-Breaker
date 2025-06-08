package dev.vpestryakov.block_breaker.ui.kit

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.blur(
  radius: Dp = 10.dp,
  overlayColor: Color? = null,
  fraction: () -> Float = { 1F },
  edgeTreatment: BlurredEdgeTreatment = BlurredEdgeTreatment.Rectangle,
): Modifier {
  val clip: Boolean
  val tileMode: TileMode

  when {
    edgeTreatment.shape != null -> {
      clip = true
      tileMode = TileMode.Clamp
    }

    else -> {
      clip = false
      tileMode = TileMode.Decal
    }
  }

  return if (radius > 0.dp || clip) {
    graphicsLayer {
      val radiusBlurPixels = radius.toPx() * fraction.invoke()
      this.renderEffect =
        if (radiusBlurPixels > 0F) {
          BlurEffect(radiusX = radiusBlurPixels, radiusY = radiusBlurPixels, edgeTreatment = tileMode)
        } else {
          null
        }

      if (overlayColor != null) {
        this.colorFilter = ColorFilter.tint(overlayColor, BlendMode.Overlay)
      }

      this.shape = edgeTreatment.shape ?: RectangleShape
      this.clip = clip
    }
  } else {
    this
  }
}
