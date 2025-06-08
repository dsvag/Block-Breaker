package dev.vpestryakov.block_breaker.dto

data class Saver<I : Any, O : Any>(val from: (I) -> O, val to: (O) -> I)
