import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.metro)
}

val googleServicesFile = file("google-services.json")
val hasGoogleServices = googleServicesFile.exists()
val hasDebugGoogleServices = hasGoogleServices &&
  googleServicesFile.readText().contains("\"dev.vpestryakov.block_breaker.dev\"")

if (hasGoogleServices) {
  apply(plugin = libs.plugins.google.services.get().pluginId)
  apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
  if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use(::load)
  }
}

val appVersionCode = providers.gradleProperty("versionCode")
  .orElse(providers.environmentVariable("ANDROID_VERSION_CODE"))
  .map { it.trim().toInt() }
  .getOrElse(1)

val appVersionName = providers.gradleProperty("versionName")
  .orElse(providers.environmentVariable("PACKAGE_VERSION"))
  .map { it.trim().removePrefix("v") }
  .filter { it.isNotEmpty() }
  .getOrElse("1.0.0")

android {
  namespace = "dev.vpestryakov.block_breaker"
  compileSdk = libs.versions.sdk.target.get().toInt()

  defaultConfig {
    applicationId = "dev.vpestryakov.block_breaker"
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()
    versionCode = appVersionCode
    versionName = appVersionName
  }

  signingConfigs {
    val storeFilePath = keystoreProperties.getProperty("storeFile") ?: System.getenv("KEYSTORE_FILE")
    if (!storeFilePath.isNullOrBlank()) {
      create("release") {
        storeFile = rootProject.file(storeFilePath)
        storePassword = keystoreProperties.getProperty("storePassword") ?: System.getenv("KEYSTORE_PASSWORD")
        keyAlias = keystoreProperties.getProperty("keyAlias") ?: System.getenv("KEY_ALIAS")
        keyPassword = keystoreProperties.getProperty("keyPassword") ?: System.getenv("KEY_PASSWORD")
      }
    }
  }

  buildTypes {
    debug {
      isDebuggable = true
      isMinifyEnabled = false
      isShrinkResources = false
      applicationIdSuffix = ".dev"
      if (hasGoogleServices && !hasDebugGoogleServices) {
        (this as ExtensionAware).extensions
          .findByType(com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension::class.java)
          ?.mappingFileUploadEnabled = false
      }
    }

    release {
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      signingConfigs.findByName("release")?.let { signingConfig = it }
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  testOptions { unitTests.isIncludeAndroidResources = true }

  buildFeatures { compose = true }
}

kotlin {
  jvmToolchain(21)

  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)

    freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
  }
}

dependencies {
  implementation(project(":shared"))
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.animation)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui.fonts)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.data.store)
  implementation(libs.androidx.lifecycle.runtime)
  implementation(libs.androidx.lifecycle.viewmodel)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.splashscreen)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.metrox.viewmodel.compose)
}

if (hasGoogleServices && !hasDebugGoogleServices) {
  tasks.matching {
    it.name.contains("DebugGoogleServices") ||
      it.name.contains("DebugCrashlytics") ||
      (it.name.contains("Crashlytics") && it.name.endsWith("Debug"))
  }.configureEach {
    enabled = false
  }
}
