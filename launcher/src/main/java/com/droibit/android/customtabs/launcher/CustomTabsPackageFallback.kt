package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import com.droibit.android.customtabs.launcher.internal.CustomTabsPackage

/**
 * If Chrome is not installed, it provides an opportunity to `CustomTabsIntent`
 * to specify a browser that support Custom Tabs as the launch target.
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