package dev.vpestryakov.block_breaker

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.MutableCreationExtras
import dev.vpestryakov.block_breaker.ui.theme.BlockBreakerTheme

class MainActivity : ComponentActivity() {

  @SuppressLint("SourceLockedOrientationActivity")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    setContent {
      val extras = remember { MutableCreationExtras().apply { set(FieldViewModel.ApplicationKey, application) } }
      BlockBreakerTheme { Content(extras) }
    }
  }
}
