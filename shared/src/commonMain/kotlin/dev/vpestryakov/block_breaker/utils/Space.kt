package dev.vpestryakov.block_breaker.utils

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
@NonRestartableComposable
fun VerticalSpace(height: Dp) {
  Spacer(modifier = Modifier.height(height = height))
}

@Composable
@NonRestartableComposable
fun HorizontalSpace(width: Dp) {
  Spacer(modifier = Modifier.width(width = width))
}

@Composable
@NonRestartableComposable
fun ColumnScope.ExpandedSpace(
  @FloatRange(from = 0.0, fromInclusive = false) weight: Float = 1F,
) {
  Spacer(modifier = Modifier.weight(weight = weight))
}

@Composable
@NonRestartableComposable
fun RowScope.ExpandedSpace(
  @FloatRange(from = 0.0, fromInclusive = false) weight: Float,
) {
  Spacer(modifier = Modifier.weight(weight = weight))
}

@Composable
@NonRestartableComposable
fun NavigationBarsSpace() {
  Spacer(modifier = Modifier.navigationBarsPadding())
}

@Composable
@NonRestartableComposable
fun StatusBarsSpace() {
  Spacer(modifier = Modifier.statusBarsPadding())
}

@Composable
@NonRestartableComposable
fun NavigationBarsWithImePaddingSpace() {
  Spacer(modifier = Modifier.navigationBarsPadding().imePadding())
}
