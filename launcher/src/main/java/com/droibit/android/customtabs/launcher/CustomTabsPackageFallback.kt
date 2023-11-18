package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import com.droibit.android.customtabs.launcher.internal.CustomTabsPackage

/**
 * The [CustomTabsPackageFallback] interface allows you
 * to specify the package name of a browser that supports Custom Tabs to the [CustomTabsIntent].
 * This means that you can launch a URL in the specified browser
 * even if Chrome is not installed or the default browser does not support Custom Tabs.
 */
fun interface CustomTabsPackageFallback {
    fun CustomTabsIntent.setCustomTabsPackage(context: Context)
}

/**
 * Fallback for launch a non-Chrome browser that supports Custom Tabs.
 *
 * @param customTabsPackages Package list of non-Chrome browsers supporting Custom Tabs. The top of the list is used with the highest priority.
 */
class NonChromeCustomTabs(
    private val customTabsPackages: List<String>
) : CustomTabsPackageFallback {

    constructor(context: Context) : this(
        CustomTabsPackage.getNonChromeCustomTabsPackages(context)
    )

    override fun CustomTabsIntent.setCustomTabsPackage(context: Context) {
        if (customTabsPackages.isNotEmpty()) {
            setCustomTabsPackage(context, customTabsPackages, fallback = null)
        }
    }
}