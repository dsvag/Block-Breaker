package dev.vpestryakov.block_breaker.dto

data class Game(
  val field: Field,
  val blocks: Blocks,
  val score: Long,
  val topScore: Long,
) {

  val dialog =
    field.clearFilledLines().first.let { effectiveField ->
      !(blocks.first?.let(effectiveField::canPlaceBlockAny) == true ||
          blocks.second?.let(effectiveField::canPlaceBlockAny) == true ||
          blocks.third?.let(effectiveField::canPlaceBlockAny) == true)
    }
}
