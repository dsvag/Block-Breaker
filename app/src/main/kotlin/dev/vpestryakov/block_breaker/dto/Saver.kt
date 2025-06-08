package dev.vpestryakov.block_breaker.dto

data class Saver<I : Any, O : Any>(
  val default: () -> I,
  val from: (I) -> O,
  val to: (O) -> I,
) {

  constructor(
    default: I,
    from: (I) -> O,
    to: (O) -> I,
  ) : this(
    default = { default },
    from = from,
    to = to,
  )

  fun toOrDefault(o: O?): I = o?.let { runCatching { to(it) }.getOrNull() } ?: default()
}
