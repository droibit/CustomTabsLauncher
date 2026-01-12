import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.maven.publish)
}

android {
  namespace = "com.droibit.android.customtabs.launcher"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = 19
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_1_8
    apiVersion = KotlinVersion.KOTLIN_1_8
    languageVersion = KotlinVersion.KOTLIN_1_8
  }
}

dependencies {
  implementation(libs.androidx.browser)

  testImplementation(libs.junit)
  testImplementation(libs.androidx.test.junit)
  testImplementation(libs.robolectric)
  testImplementation(libs.mockk)
  testImplementation(libs.truth)
}

mavenPublishing {
  configure(
    AndroidSingleVariantLibrary(
      variant = "release",
      sourcesJar = true,
      publishJavadocJar = false,
    ),
  )
}
