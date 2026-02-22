import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
}

android {
  namespace = "com.droibit.android.customtabs.launcher.example"
  compileSdk {
    version = release(libs.versions.compileSdk.get().toInt())
  }

  defaultConfig {
    applicationId = "com.droibit.android.customtabs.launcher.example"
    minSdk = 23
    targetSdk {
      release(35)
    }
    versionCode = 1
    versionName = "1.0.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  buildFeatures {
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_17
  }
}

dependencies {
  implementation(projects.launcher)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.browser)
}
