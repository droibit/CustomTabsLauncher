plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin)
}

android {
  namespace = "com.droibit.android.customtabs.launcher.example"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.droibit.android.customtabs.launcher.example"
    minSdk = 23
    targetSdk = 35
    versionCode = 1
    versionName = "1.0.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }

  buildFeatures {
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
}

dependencies {
  implementation(projects.launcher)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.browser)
}
