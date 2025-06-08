package dev.vpestryakov.block_breaker.dto

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Immutable
data class Field private constructor(private val list: MutableList<Boolean>) : MutableList<Boolean> by list {

  val dimension = sqrt(list.size.toDouble()).roundToInt()

  fun canPlaceBlockAny(block: Block): Boolean {
    val maxX = dimension - block.size.width + 1
    val maxY = dimension - block.size.height + 1

    for (y in 0 until maxY) {
      for (x in 0 until maxX) {
        if (canPlaceBlock(block = block, coordinates = IntOffset(x, y))) return true
      }
    }

    return false
  }

  fun canPlaceBlock(block: Block, coordinates: IntOffset): Boolean =
    block.list.all { (dx, dy) ->
      val index = (coordinates.y + dy) * dimension + (coordinates.x + dx)
      getOrNull(index) == false
    }

  fun clearFilledLines(): Field {
    val rowFlags = BooleanArray(dimension) { true }
    val colFlags = BooleanArray(dimension) { true }

    for (i in 0 until dimension) {
      for (j in 0 until dimension) {
        if (!list[i * dimension + j]) rowFlags[i] = false
        if (!list[j * dimension + i]) colFlags[i] = false
      }
    }

    rowFlags.forEachIndexed { i, filled -> if (filled) for (j in 0 until dimension) list[i * dimension + j] = false }
    colFlags.forEachIndexed { j, filled -> if (filled) for (i in 0 until dimension) list[i * dimension + j] = false }

    return copy()
  }

  fun placeBlock(block: Block, coordinates: IntOffset): Field {
    block.list.forEach { (dx, dy) ->
      val x = coordinates.x + dx
      val y = coordinates.y + dy
      val index = y * dimension + x

      list[index] = true
    }

    return copy()
  }

  companion object {

    private const val DefaultSize = 10

    val Empty = Field(MutableList(DefaultSize * DefaultSize) { false })

    val Saver =
      Saver<Field, String>(
        from = { it.list.joinToString(separator = "", transform = { bit -> if (bit) "1" else "0" }) },
        to = { it.map { bit -> bit == '1' }.toMutableList().let(::Field) },
      )
  }
}
