package dev.vpestryakov.block_breaker.dto

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntSize

@Immutable
data class Block private constructor(val list: List<Pair<Int, Int>>) {

  private constructor(vararg list: Pair<Int, Int>) : this(list.toList())

  val size: IntSize =
    list
      .foldRight(
        initial = 1 to 1,
        operation = { coor, acc -> maxOf(coor.first + 1, acc.first) to maxOf(coor.second + 1, acc.second) },
      )
      .let { (width, height) -> IntSize(width, height) }

  val spacingCount =
    list.foldRight(
      initial = 0 to 0,
      operation = { coor, acc -> maxOf(coor.first, acc.first) to maxOf(coor.second, acc.second) },
    )

  companion object {

    private val Square1 = Block(0 to 0)
    private val Square2 = Block(0 to 0, 0 to 1, 1 to 0, 1 to 1)
    private val Square3 = Block(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2, 2 to 0, 2 to 1, 2 to 2)

    private val Stick2Vertically = Block(0 to 0, 0 to 1)
    private val Stick2Horizontally = Block(0 to 0, 1 to 0)

    private val Stick3Vertically = Block(0 to 0, 0 to 1, 0 to 2)
    private val Stick3Horizontally = Block(0 to 0, 1 to 0, 2 to 0)

    private val Stick4Vertically = Block(0 to 0, 0 to 1, 0 to 2, 0 to 3)
    private val Stick4Horizontally = Block(0 to 0, 1 to 0, 2 to 0, 3 to 0)

    private val Stick5Vertically = Block(0 to 0, 0 to 1, 0 to 2, 0 to 3, 0 to 4)
    private val Stick5Horizontally = Block(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0)

    private val Coner2Up = Block(0 to 0, 0 to 1, 1 to 0)
    private val Coner2Right = Block(0 to 0, 1 to 0, 1 to 1)
    private val Coner2Down = Block(0 to 1, 1 to 0, 1 to 1)
    private val Coner2Left = Block(0 to 0, 0 to 1, 1 to 1)

    private val Coner3Up = Block(0 to 0, 0 to 1, 0 to 2, 1 to 0, 2 to 0)
    private val Coner3Right = Block(0 to 0, 2 to 1, 2 to 2, 1 to 0, 2 to 0)
    private val Coner3Down = Block(0 to 2, 1 to 2, 2 to 0, 2 to 1, 2 to 2)
    private val Coner3Left = Block(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2)

    private val ZRightHorizontal = Block(0 to 0, 1 to 0, 1 to 1, 2 to 1)
    private val ZRightVertically = Block(1 to 0, 1 to 1, 0 to 1, 0 to 2)

    private val ZLeftHorizontal = Block(0 to 1, 1 to 1, 1 to 0, 2 to 0)
    private val ZLeftVertically = Block(0 to 0, 0 to 1, 1 to 1, 1 to 2)

    private val TUp = Block(1 to 0, 0 to 1, 1 to 1, 2 to 1)
    private val TRight = Block(0 to 0, 0 to 1, 0 to 2, 1 to 1)
    private val TDown = Block(0 to 0, 1 to 0, 2 to 0, 1 to 1)
    private val TLeft = Block(1 to 0, 1 to 1, 1 to 2, 0 to 1)

    private val Plus = Block(1 to 0, 0 to 1, 1 to 1, 2 to 1, 1 to 2)

    private val set =
      setOf<Block>(
        Square1,
        Square2,
        Square3,
        Stick2Horizontally,
        Stick2Vertically,
        Stick3Horizontally,
        Stick3Vertically,
        Stick4Horizontally,
        Stick4Vertically,
        Stick5Horizontally,
        Stick5Vertically,
        Coner2Up,
        Coner2Right,
        Coner2Down,
        Coner2Left,
        Coner3Up,
        Coner3Right,
        Coner3Down,
        Coner3Left,
        ZRightHorizontal,
        ZRightVertically,
        ZLeftHorizontal,
        ZLeftVertically,
        TUp,
        TRight,
        TDown,
        TLeft,
        Plus,
      )

    fun random(): Block = set.random()
  }
}
