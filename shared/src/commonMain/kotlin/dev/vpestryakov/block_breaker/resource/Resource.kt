package dev.vpestryakov.block_breaker.resource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Drawables {

  val ic_settings: ImageVector by lazy {
    ImageVector.Builder(
      name = "ic_settings",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
      ) {
        moveTo(7.843f, 3.802f)
        curveTo(9.872f, 2.601f, 10.886f, 2f, 12f, 2f)
        reflectiveCurveToRelative(2.128f, 0.6f, 4.157f, 1.802f)
        lineToRelative(0.686f, 0.406f)
        curveToRelative(2.029f, 1.202f, 3.043f, 1.803f, 3.6f, 2.792f)
        curveToRelative(0.557f, 0.99f, 0.557f, 2.19f, 0.557f, 4.594f)
        verticalLineToRelative(0.812f)
        curveToRelative(0f, 2.403f, 0f, 3.605f, -0.557f, 4.594f)
        reflectiveCurveToRelative(-1.571f, 1.59f, -3.6f, 2.791f)
        lineToRelative(-0.686f, 0.407f)
        curveTo(14.128f, 21.399f, 13.114f, 22f, 12f, 22f)
        reflectiveCurveToRelative(-2.128f, -0.6f, -4.157f, -1.802f)
        lineToRelative(-0.686f, -0.407f)
        curveToRelative(-2.029f, -1.2f, -3.043f, -1.802f, -3.6f, -2.791f)
        curveTo(3f, 16.01f, 3f, 14.81f, 3f, 12.406f)
        verticalLineToRelative(-0.812f)
        curveTo(3f, 9.19f, 3f, 7.989f, 3.557f, 7f)
        reflectiveCurveToRelative(1.571f, -1.59f, 3.6f, -2.792f)
        close()
      }
      path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
      ) {
        moveTo(12f, 12f)
        moveToRelative(-3f, 0f)
        arcToRelative(3f, 3f, 0f, true, true, 6f, 0f)
        arcToRelative(3f, 3f, 0f, true, true, -6f, 0f)
      }
    }.build()
  }

  val ic_sun: ImageVector by lazy {
    ImageVector.Builder(
      name = "ic_sun",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
      ) {
        moveTo(12f, 12f)
        moveToRelative(-5f, 0f)
        arcToRelative(5f, 5f, 0f, true, true, 10f, 0f)
        arcToRelative(5f, 5f, 0f, true, true, -10f, 0f)
      }
      path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
      ) {
        moveTo(12f, 2f)
        verticalLineToRelative(2f)
        moveToRelative(0f, 16f)
        verticalLineToRelative(2f)
        moveTo(4f, 12f)
        horizontalLineTo(2f)
        moveToRelative(20f, 0f)
        horizontalLineToRelative(-2f)
        moveToRelative(-0.222f, -7.777f)
        lineToRelative(-2.222f, 2.031f)
        moveTo(4.222f, 4.223f)
        lineToRelative(2.222f, 2.031f)
        moveToRelative(0f, 11.302f)
        lineToRelative(-2.222f, 2.222f)
        moveToRelative(15.556f, -0.001f)
        lineToRelative(-2.222f, -2.222f)
      }
    }.build()
  }

  val ic_moon: ImageVector by lazy {
    ImageVector.Builder(
      name = "ic_moon",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      path(
        fill = SolidColor(Color.Black),
      ) {
        moveToRelative(21.067f, 11.857f)
        lineToRelative(-0.642f, -0.388f)
        close()
        moveTo(12.143f, 2.933f)
        lineTo(11.755f, 2.291f)
        close()
        moveTo(21.25f, 12f)
        arcTo(9.25f, 9.25f, 0f, false, true, 12f, 21.25f)
        verticalLineToRelative(1.5f)
        curveToRelative(5.937f, 0f, 10.75f, -4.813f, 10.75f, -10.75f)
        close()
        moveTo(12f, 21.25f)
        arcTo(9.25f, 9.25f, 0f, false, true, 2.75f, 12f)
        horizontalLineToRelative(-1.5f)
        curveToRelative(0f, 5.937f, 4.813f, 10.75f, 10.75f, 10.75f)
        close()
        moveTo(2.75f, 12f)
        arcTo(9.25f, 9.25f, 0f, false, true, 12f, 2.75f)
        verticalLineToRelative(-1.5f)
        curveTo(6.063f, 1.25f, 1.25f, 6.063f, 1.25f, 12f)
        close()
        moveTo(15.5f, 14.25f)
        arcTo(5.75f, 5.75f, 0f, false, true, 9.75f, 8.5f)
        horizontalLineToRelative(-1.5f)
        arcToRelative(7.25f, 7.25f, 0f, false, false, 7.25f, 7.25f)
        close()
        moveTo(20.425f, 11.469f)
        arcTo(5.75f, 5.75f, 0f, false, true, 15.5f, 14.25f)
        verticalLineToRelative(1.5f)
        arcToRelative(7.25f, 7.25f, 0f, false, false, 6.21f, -3.505f)
        close()
        moveTo(9.75f, 8.5f)
        arcToRelative(5.75f, 5.75f, 0f, false, true, 2.781f, -4.925f)
        lineToRelative(-0.776f, -1.284f)
        arcTo(7.25f, 7.25f, 0f, false, false, 8.25f, 8.5f)
        close()
        moveTo(12f, 2.75f)
        arcToRelative(0.38f, 0.38f, 0f, false, true, -0.268f, -0.118f)
        arcToRelative(0.3f, 0.3f, 0f, false, true, -0.082f, -0.155f)
        curveToRelative(-0.004f, -0.031f, -0.002f, -0.121f, 0.105f, -0.186f)
        lineToRelative(0.776f, 1.284f)
        curveToRelative(0.503f, -0.304f, 0.665f, -0.861f, 0.606f, -1.299f)
        curveToRelative(-0.062f, -0.455f, -0.42f, -1.026f, -1.137f, -1.026f)
        close()
        moveTo(21.71f, 12.245f)
        curveToRelative(-0.066f, 0.107f, -0.156f, 0.109f, -0.187f, 0.105f)
        arcToRelative(0.3f, 0.3f, 0f, false, true, -0.155f, -0.082f)
        arcToRelative(0.38f, 0.38f, 0f, false, true, -0.118f, -0.268f)
        horizontalLineToRelative(1.5f)
        curveToRelative(0f, -0.717f, -0.571f, -1.075f, -1.026f, -1.137f)
        curveToRelative(-0.438f, -0.059f, -0.995f, 0.103f, -1.299f, 0.606f)
        close()
      }
    }.build()
  }

  val ic_watch: ImageVector by lazy {
    ImageVector.Builder(
      name = "ic_watch",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
      ) {
        moveTo(5f, 12f)
        curveToRelative(0f, -2.809f, 0f, -4.213f, 0.674f, -5.222f)
        arcToRelative(4f, 4f, 0f, false, true, 1.104f, -1.104f)
        curveTo(7.787f, 5f, 9.19f, 5f, 12f, 5f)
        reflectiveCurveToRelative(4.213f, 0f, 5.222f, 0.674f)
        arcToRelative(4f, 4f, 0f, false, true, 1.104f, 1.104f)
        curveTo(19f, 7.787f, 19f, 9.19f, 19f, 12f)
        reflectiveCurveToRelative(0f, 4.213f, -0.674f, 5.222f)
        arcToRelative(4f, 4f, 0f, false, true, -1.104f, 1.104f)
        curveTo(16.213f, 19f, 14.81f, 19f, 12f, 19f)
        reflectiveCurveToRelative(-4.213f, 0f, -5.222f, -0.674f)
        arcToRelative(4f, 4f, 0f, false, true, -1.104f, -1.104f)
        curveTo(5f, 16.213f, 5f, 14.81f, 5f, 12f)
        close()
      }
      path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
      ) {
        moveTo(12f, 9f)
        verticalLineToRelative(3.077f)
        lineTo(14f, 14f)
      }
    }.build()
  }
}
