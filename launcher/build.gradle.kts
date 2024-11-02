plugins {
    id("com.android.library")
    id("kotlin-android")
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(libs.androidx.browser)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.truth)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.robolectric)
}

apply(from = "$rootDir/gradle/gradle-mvn-push.gradle.kts")