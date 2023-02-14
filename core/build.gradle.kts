plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.kotlin.plugin.serialization)
}

android {
  namespace = "com.dino.core"
  compileSdk = 33

  defaultConfig {
    minSdk = 22
    targetSdk = 33

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildFeatures {
    aidl = false
    buildConfig = false
    renderScript = false
    shaders = false
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {

  // Arch Components
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.kotlinx.coroutines.android)

  // Ktor
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.serialization)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.negotiation)
  implementation(libs.ktor.serialization.json)

  // Kotlinx Serialization Json
  implementation(libs.kotlinx.serialization.json)

  // Local tests: jUnit, coroutines, Android runner
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)

}