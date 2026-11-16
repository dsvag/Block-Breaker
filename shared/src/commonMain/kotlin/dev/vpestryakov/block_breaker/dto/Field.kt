package dev.vpestryakov.block_breaker.dto

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastForEach
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Field private constructor(private val list: List<BlockColor?>) : List<BlockColor?> by list {

  val dimension = sqrt(list.size.toDouble()).roundToInt()

  fun canPlaceBlockAny(block: Block): Boolean {
    val maxX = dimension - block.size.width + 1
    val maxY = dimension - block.size.height + 1

    for (y in 0 until maxY) {
      for (x in 0 until maxX) {
        if (canPlaceBlockAt(block = block, coordinates = IntOffset(x, y))) return true
      }
    }

    return false
  }

  fun canPlaceBlockAt(block: Block, coordinates: IntOffset): Boolean =
    block.list.fastAll { offset ->
      val x = coordinates.x + offset.x
      val y = coordinates.y + offset.y
      if (x !in 0 until dimension || y !in 0 until dimension) return@fastAll false
      val index = y * dimension + x
      index in list.indices && list[index] == null
    }

  fun clearFilledLines(): Pair<Field, Int> {
    val rowFlags = BooleanArray(dimension) { true }
    val colFlags = BooleanArray(dimension) { true }

    for (i in 0 until dimension) {
      for (j in 0 until dimension) {
        if (list[i * dimension + j] == null) {
          rowFlags[i] = false
          colFlags[j] = false
        }
      }
    }

    val clearedLines = rowFlags.count { it } + colFlags.count { it }
    if (clearedLines == 0) return this to 0

    val updated =
      list.toMutableList().apply {
        rowFlags.forEachIndexed { i, filled ->
          if (filled) for (j in 0 until dimension) this[i * dimension + j] = null
        }
        colFlags.forEachIndexed { j, filled ->
          if (filled) for (i in 0 until dimension) this[i * dimension + j] = null
        }
      }

    return Field(updated) to clearedLines
  }

  fun placeBlock(block: Block, coordinates: IntOffset): Field {
    val updated =
      list.toMutableList().apply {
        block.list.fastForEach { offset ->
          val x = coordinates.x + offset.x
          val y = coordinates.y + offset.y
          val index = y * dimension + x
          this[index] = block.color
        }
      }

    return Field(updated)
  }

  companion object {

    private const val DefaultSize = 10

    val Empty = Field(List<BlockColor?>(DefaultSize * DefaultSize) { null })

    val Saver =
      Saver(
        default = Empty,
        from = { field ->
          field.list.joinToString(separator = "", transform = { it.storageIndex.toString() })
        },
        to = { raw ->
          val parsed = raw.map { ch -> BlockColor.fromIndexOrNull(ch.digitToIntOrNull() ?: 0) }
          if (parsed.size == DefaultSize * DefaultSize) Field(parsed) else Empty
        },
      )
  }
}

