@file:Suppress("NOTHING_TO_INLINE")

package dev.vpestryakov.block_breaker.utils

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

inline fun Dp.toPx(density: Density): Float = with(density) { toPx() }

inline fun TextUnit.toDp(density: Density): Dp = with(density) { toDp() }

inline fun TextUnit.toPx(density: Density): Float = with(density) { toPx() }

inline fun Dp.roundToPx(density: Density): Int = with(density) { roundToPx() }

inline fun Float.toDp(density: Density): Dp = with(density) { toDp() }

inline fun Int.toDp(density: Density): Dp = with(density) { toDp() }

inline fun Int.spAsDp(density: Density): Dp = with(density) { sp.toDp() }
