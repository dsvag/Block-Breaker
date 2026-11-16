import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
}

kotlin {
  jvmToolchain(21)

  android {
    namespace = "dev.vpestryakov.block_breaker.shared"
    compileSdk = libs.versions.sdk.target.get().toInt()
    minSdk = libs.versions.sdk.min.get().toInt()

    androidResources.enable = true

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_21)
    }
  }

  jvm("desktop") {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_21)
    }
  }

  listOf(
    iosArm64(),
    iosSimulatorArm64(),
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "Shared"
      isStatic = true
      binaryOption("bundleId", "dev.vpestryakov.blockbreaker.shared")
    }
  }

  macosArm64 {
    binaries.executable {
      entryPoint = "dev.vpestryakov.block_breaker.main"
    }
  }

  compilerOptions {
    freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
  }

  sourceSets {
    commonMain.dependencies {
      api(libs.androidx.annotation)
      api(libs.androidx.data.store)
      api(libs.androidx.data.store.core)
      api(libs.compose.animation)
      api(libs.compose.components.resources)
      api(libs.compose.foundation)
      api(libs.compose.runtime)
      api(libs.compose.ui)
      api(libs.jetbrains.lifecycle.runtime.compose)
      api(libs.jetbrains.lifecycle.viewmodel)
      api(libs.jetbrains.lifecycle.viewmodel.compose)
      api(libs.kotlinx.coroutines.core)
      api(libs.metrox.viewmodel)
      api(libs.metrox.viewmodel.compose)
      api(libs.okio)
    }

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.compose.ui.fonts)
      implementation(libs.androidx.core.ktx)
      implementation(libs.kotlinx.coroutines.android)
    }

    named("desktopMain") {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.kotlinx.coroutines.swing)
      }
    }
  }
}

compose.resources {
  publicResClass = true
  packageOfResClass = "dev.vpestryakov.block_breaker.shared.generated.resources"
}

compose.desktop {
  application {
    mainClass = "dev.vpestryakov.block_breaker.MainKt"

    nativeDistributions {
      targetFormats(
        TargetFormat.Dmg,
        TargetFormat.Msi,
        TargetFormat.Exe,
        TargetFormat.Deb,
      )
      packageName = "Block Breaker"
      packageVersion = System.getenv("PACKAGE_VERSION")
        ?.removePrefix("v")
        ?.takeIf { it.matches(Regex("""^\d+\.\d+\.\d+$""")) }
        ?: "1.0.0"
      description = "Block Breaker 10x10 Puzzle Game"
      vendor = "Vitaly Pestryakov"

      macOS {
        bundleID = "dev.vpestryakov.block_breaker"
        val signIdentity = System.getenv("MACOS_SIGN_IDENTITY")
        if (!signIdentity.isNullOrBlank()) {
          signing {
            sign.set(true)
            identity.set(signIdentity)
            System.getenv("MACOS_KEYCHAIN")?.takeIf { it.isNotBlank() }?.let { keychain.set(it) }
          }
          val notarizeAppleId = System.getenv("MACOS_NOTARIZATION_APPLE_ID")
          val notarizePassword = System.getenv("MACOS_NOTARIZATION_PASSWORD")
          val notarizeTeamId = System.getenv("MACOS_NOTARIZATION_TEAM_ID")
          if (!notarizeAppleId.isNullOrBlank() && !notarizePassword.isNullOrBlank() && !notarizeTeamId.isNullOrBlank()) {
            notarization {
              appleID.set(notarizeAppleId)
              password.set(notarizePassword)
              teamID.set(notarizeTeamId)
            }
          }
        }
      }

      windows {
        menuGroup = "Block Breaker"
        upgradeUuid = "8f4e3b2a-9c1d-4e5f-a6b7-c8d9e0f1a2b3"
        shortcut = true
      }

      linux {
        packageName = "block-breaker"
        shortcut = true
      }
    }
  }
}

