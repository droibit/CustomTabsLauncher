package com.droibit.android.customtabs.launcher.internal

internal object ChromePackage {
    const val PACKAGE_STABLE = "com.android.chrome"
    const val PACKAGE_BETA = "com.chrome.beta"
    const val PACKAGE_DEV = "com.chrome.dev"
    const val PACKAGE_LOCAL = "com.google.android.apps.chrome"

    val CHROME_PACKAGES = listOf(PACKAGE_STABLE, PACKAGE_BETA, PACKAGE_DEV, PACKAGE_LOCAL)
}