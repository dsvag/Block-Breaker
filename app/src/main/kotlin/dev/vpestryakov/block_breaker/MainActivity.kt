package dev.vpestryakov.block_breaker

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.vpestryakov.block_breaker.ui.Content
import dev.vpestryakov.block_breaker.ui.theme.BlockBreakerTheme
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

class MainActivity : ComponentActivity() {

  private lateinit var component: ActivityComponent

  @SuppressLint("SourceLockedOrientationActivity")
  override fun onCreate(savedInstanceState: Bundle?) {
    val splash = installSplashScreen()

    super.onCreate(savedInstanceState)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    enableEdgeToEdge()

    val themeHolder = component.themeHolder
    val initialIsDark =
      (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    var themeLoaded = false
    var gameLoaded = false

    splash.setKeepOnScreenCondition { !themeLoaded || !gameLoaded }
    splash.setOnExitAnimationListener { splashView ->
      val remainingAnimMs =
        (splashView.iconAnimationStartMillis + splashView.iconAnimationDurationMillis - System.currentTimeMillis())
          .coerceIn(0L, 450L)

      val rootFade =
        ObjectAnimator.ofFloat(splashView.view, View.ALPHA, 1f, 0f).apply {
          duration = 320L
          startDelay = remainingAnimMs + 80L
          interpolator = DecelerateInterpolator()
        }

      val icon = splashView.iconView
      val scaleX =
        ObjectAnimator.ofFloat(icon, View.SCALE_X, 1f, 0f).apply {
          duration = 360L
          startDelay = remainingAnimMs
          interpolator = AnticipateInterpolator(2.2f)
        }
      val scaleY =
        ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1f, 0f).apply {
          duration = 360L
          startDelay = remainingAnimMs
          interpolator = AnticipateInterpolator(2.2f)
        }
      val rotation =
        ObjectAnimator.ofFloat(icon, View.ROTATION, 0f, 14f).apply {
          duration = 360L
          startDelay = remainingAnimMs
          interpolator = AnticipateInterpolator(1.8f)
        }

      AnimatorSet().apply {
        playTogether(rootFade, scaleX, scaleY, rotation)
        doOnEnd { splashView.remove() }
        start()
      }
    }

    setContent {
      val isDarkTheme by
      produceState(initialValue = initialIsDark) {
        themeHolder.isDarkThemeFlow.collect {
          value = it
          themeLoaded = true
        }
      }

      CompositionLocalProvider(
        LocalMetroViewModelFactory provides component.metroViewModelFactory,
      ) {
        BlockBreakerTheme(isDarkTheme) {
          Content(onGameLoaded = { gameLoaded = true })
        }
      }
    }
  }

  override fun attachBaseContext(newBase: Context) {
    initComponent(newBase)
    val context = component.themeConfigurationOverrider.overrideContext(newBase)
    super.attachBaseContext(context)
  }

  private fun initComponent(context: Context) {
    component = (context.applicationContext as App).appComponent.activityComponent
  }
}
