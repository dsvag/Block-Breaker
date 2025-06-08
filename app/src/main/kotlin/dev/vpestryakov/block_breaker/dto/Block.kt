package dev.vpestryakov.block_breaker.dto

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastForEach

data class Blocks(
  val first: Block?,
  val second: Block?,
  val third: Block?,
) {

  val isEmpty = first == null && second == null && third == null

  fun get(index: Int): Block? =
    when (index) {
      0 -> first
      1 -> second
      2 -> third
      else -> null
    }

  fun remove(index: Int): Blocks =
    when (index) {
      0 -> copy(first = null)
      1 -> copy(second = null)
      2 -> copy(third = null)
      else -> this
    }

  companion object {

    fun random(): Blocks =
      Blocks(
        first = Block.random(),
        second = Block.random(),
        third = Block.random(),
      )

    val Saver =
      Saver<Blocks, String>(
        default = ::random,
        from = { (first, second, third) ->
          buildString {
            first?.let { append(Block.Saver.from(it)) }
            append('&')
            second?.let { append(Block.Saver.from(it)) }
            append('&')
            third?.let { append(Block.Saver.from(it)) }
          }
        },
        to = {
          it
            .split('&')
            .map { block ->
              block.takeIf(String::isNotEmpty)?.let { str -> runCatching { Block.Saver.to(str) }.getOrNull() }
            }
            .let { blocks ->
              Blocks(
                first = blocks.getOrNull(0),
                second = blocks.getOrNull(1),
                third = blocks.getOrNull(2),
              )
            }
            .let { blocks -> if (blocks.isEmpty) random() else blocks }
        },
      )
  }
}

enum class BlockColor {
  Coral,
  Amber,
  Mint,
  Teal,
  Sky,
  Lavender,
  Rose;

  companion object {
    fun fromIndexOrNull(index: Int): BlockColor? = entries.getOrNull(index - 1)
  }
}

val BlockColor?.storageIndex: Int
  get() = if (this == null) 0 else ordinal + 1

data class Block private constructor(
  val list: List<IntOffset>,
  val color: BlockColor,
) {

  private constructor(
    color: BlockColor,
    vararg list: Pair<Int, Int>,
  ) : this(list.map { (x, y) -> IntOffset(x, y) }, color)

  val cellsCount = list.size

  val size: IntSize

  val spacingCount: IntOffset

  init {
    var maxWidth = 1
    var maxHeight = 1
    var maxSpacingX = 0
    var maxSpacingY = 0
    list.fastForEach { coor ->
      maxWidth = maxOf(coor.x + 1, maxWidth)
      maxHeight = maxOf(coor.y + 1, maxHeight)
      maxSpacingX = maxOf(coor.x, maxSpacingX)
      maxSpacingY = maxOf(coor.y, maxSpacingY)
    }
    size = IntSize(maxWidth, maxHeight)
    spacingCount = IntOffset(maxSpacingX, maxSpacingY)
  }

  companion object {

    private val Square1 = Block(BlockColor.Amber, 0 to 0)
    private val Square2 = Block(BlockColor.Mint, 0 to 0, 0 to 1, 1 to 0, 1 to 1)
    private val Square3 = Block(BlockColor.Teal, 0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2, 2 to 0, 2 to 1, 2 to 2)

    private val Stick2Vertically = Block(BlockColor.Coral, 0 to 0, 0 to 1)
    private val Stick2Horizontally = Block(BlockColor.Coral, 0 to 0, 1 to 0)

    private val Stick3Vertically = Block(BlockColor.Amber, 0 to 0, 0 to 1, 0 to 2)
    private val Stick3Horizontally = Block(BlockColor.Amber, 0 to 0, 1 to 0, 2 to 0)

    private val Stick4Vertically = Block(BlockColor.Sky, 0 to 0, 0 to 1, 0 to 2, 0 to 3)
    private val Stick4Horizontally = Block(BlockColor.Sky, 0 to 0, 1 to 0, 2 to 0, 3 to 0)

    private val Stick5Vertically = Block(BlockColor.Rose, 0 to 0, 0 to 1, 0 to 2, 0 to 3, 0 to 4)
    private val Stick5Horizontally = Block(BlockColor.Rose, 0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0)

    private val Coner2Up = Block(BlockColor.Mint, 0 to 0, 0 to 1, 1 to 0)
    private val Coner2Right = Block(BlockColor.Mint, 0 to 0, 1 to 0, 1 to 1)
    private val Coner2Down = Block(BlockColor.Mint, 0 to 1, 1 to 0, 1 to 1)
    private val Coner2Left = Block(BlockColor.Mint, 0 to 0, 0 to 1, 1 to 1)

    private val Coner3Up = Block(BlockColor.Sky, 0 to 0, 0 to 1, 0 to 2, 1 to 0, 2 to 0)
    private val Coner3Right = Block(BlockColor.Sky, 0 to 0, 2 to 1, 2 to 2, 1 to 0, 2 to 0)
    private val Coner3Down = Block(BlockColor.Sky, 0 to 2, 1 to 2, 2 to 0, 2 to 1, 2 to 2)
    private val Coner3Left = Block(BlockColor.Sky, 0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2)

    private val ZRightHorizontal = Block(BlockColor.Coral, 0 to 0, 1 to 0, 1 to 1, 2 to 1)
    private val ZRightVertically = Block(BlockColor.Coral, 1 to 0, 1 to 1, 0 to 1, 0 to 2)

    private val ZLeftHorizontal = Block(BlockColor.Teal, 0 to 1, 1 to 1, 1 to 0, 2 to 0)
    private val ZLeftVertically = Block(BlockColor.Teal, 0 to 0, 0 to 1, 1 to 1, 1 to 2)

    private val TUp = Block(BlockColor.Lavender, 1 to 0, 0 to 1, 1 to 1, 2 to 1)
    private val TRight = Block(BlockColor.Lavender, 0 to 0, 0 to 1, 0 to 2, 1 to 1)
    private val TDown = Block(BlockColor.Lavender, 0 to 0, 1 to 0, 2 to 0, 1 to 1)
    private val TLeft = Block(BlockColor.Lavender, 1 to 0, 1 to 1, 1 to 2, 0 to 1)

    private val Plus = Block(BlockColor.Rose, 1 to 0, 0 to 1, 1 to 1, 2 to 1, 1 to 2)

    private val set =
      listOf(
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

    private fun colorForCoordinates(coords: List<IntOffset>): BlockColor =
      set.firstOrNull { it.list == coords }?.color ?: BlockColor.Rose

    val Saver =
      Saver<Block, String>(
        default = ::random,
        from = { block ->
          val coords = block.list.joinToString(separator = ";", transform = { "${it.x},${it.y}" })
          "${block.color.ordinal}:$coords"
        },
        to = { raw ->
          val colorSplit = raw.split(':', limit = 2)
          val coordsStr = if (colorSplit.size == 2) colorSplit[1] else colorSplit[0]
          val coords =
            coordsStr
              .split(';')
              .map { pair ->
                val (x, y) = pair.split(',').map(String::toInt)
                IntOffset(x, y)
              }
          val color =
            if (colorSplit.size == 2) {
              colorSplit[0].toIntOrNull()?.let(BlockColor.entries::getOrNull) ?: colorForCoordinates(coords)
            } else {
              colorForCoordinates(coords)
            }
          Block(list = coords, color = color)
        },
      )
  }
}
