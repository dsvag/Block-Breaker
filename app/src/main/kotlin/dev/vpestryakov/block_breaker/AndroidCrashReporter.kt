package dev.vpestryakov.block_breaker

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Inject
class AndroidCrashReporter(
  private val context: Context,
) : CrashReporter {

  private val crashlytics: FirebaseCrashlytics? by lazy {
    if (FirebaseApp.getApps(context).isNotEmpty()) {
      runCatching { FirebaseCrashlytics.getInstance() }.getOrNull()
    } else {
      null
    }
  }

  fun initialize() {
    val instance = crashlytics ?: return
    val packageInfo = runCatching {
      context.packageManager.getPackageInfo(context.packageName, 0)
    }.getOrNull()
    packageInfo?.versionName?.let { instance.setCustomKey("app_version", it) }
  }

  override fun recordException(throwable: Throwable) {
    crashlytics?.recordException(throwable)
  }

  override fun log(message: String) {
    crashlytics?.log(message)
  }

  override fun setCustomKey(key: String, value: String) {
    crashlytics?.setCustomKey(key, value)
  }
}
