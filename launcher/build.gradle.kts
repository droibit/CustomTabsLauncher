import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.maven.publish)
}

android {
  namespace = "com.droibit.android.customtabs.launcher"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = 23
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
      javadocJar = JavadocJar.None(),
      sourcesJar = SourcesJar.Sources(),
      variant = "release",
    ),
  )
}
