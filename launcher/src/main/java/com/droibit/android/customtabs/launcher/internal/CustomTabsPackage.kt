package com.droibit.android.customtabs.launcher.internal

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsService


internal object CustomTabsPackage {
    private const val PACKAGE_CHROME_STABLE = "com.android.chrome"
    private const val PACKAGE_CHROME_BETA = "com.chrome.beta"
    private const val PACKAGE_CHROME_DEV = "com.chrome.dev"
    private const val PACKAGE_CHROME_LOCAL = "com.google.android.apps.chrome"

    val CHROME_PACKAGES =
        listOf(PACKAGE_CHROME_STABLE, PACKAGE_CHROME_BETA, PACKAGE_CHROME_DEV, PACKAGE_CHROME_LOCAL)

    fun getNonChromeCustomTabsPackages(context: Context): List<String> {
        val activityIntent = Intent(ACTION_VIEW, Uri.parse("http://"))
            .addCategory(Intent.CATEGORY_BROWSABLE)
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PackageManager.MATCH_ALL
        } else {
            PackageManager.MATCH_DEFAULT_ONLY
        }
        val pm = context.packageManager
        return pm.queryIntentActivities(activityIntent, flag)
            .asSequence()
            .map { it.activityInfo.packageName }
            .filter { it !in CHROME_PACKAGES }
            .filter {
                val serviceIntent = Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION)
                    .setPackage(it)
                pm.resolveService(serviceIntent, 0) != null
            }
            .toList()
    }
}