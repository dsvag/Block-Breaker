package dev.vpestryakov.block_breaker

interface CrashReporter {
  fun recordException(throwable: Throwable)
  fun log(message: String)
  fun setCustomKey(key: String, value: String)
}

object NoOpCrashReporter : CrashReporter {
  override fun recordException(throwable: Throwable) = Unit
  override fun log(message: String) = Unit
  override fun setCustomKey(key: String, value: String) = Unit
}
