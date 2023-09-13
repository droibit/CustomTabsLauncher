@file:JvmName("CustomTabsIntentHelper")

package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import com.droibit.android.customtabs.launcher.internal.CustomTabsPackage.CHROME_PACKAGES

/**
 * Sets the package name of the [CustomTabsIntent] to directly launch a browser that supports Custom Tabs.
 *
 * Chrome is prioritized as the launch target, but if it is not installed,
 * other browsers can be provided as the target using the [CustomTabsPackageFallback].
 * (If no browser that supports Custom Tabs is installed on the device, the CustomTabsIntent is not affected.)
 *
 * * Basic usage:
 * ```
 * val customTabsIntent = build()
 * customTabsIntent.ensureCustomTabsPackage(context)
 * customTabsIntent.launchUrl(context, Uri)
 * ```
 *
 * * Present the custom tab as bottom sheet
 * ```
 * val activityLauncher = registerForActivityResult(StartActivityForResult()) {
 *      // Do something.
 * }
 *
 * val customTabsIntent = build().apply {
 *      ensureCustomTabsPackage(context)
 *      intent.data = Uri
 * }
 * activityLauncher.launch(customTabsIntent.intent)
 * ```
 *
 * @param context The source Context
 * @param fallback A [CustomTabsPackageFallback] to be used if Chrome Custom Tabs is not available.
 */
@JvmOverloads
fun CustomTabsIntent.ensureCustomTabsPackage(
    context: Context,
    fallback: CustomTabsPackageFallback? = null,
): CustomTabsIntent {
    setCustomTabsPackage(context, CHROME_PACKAGES, fallback)
    return this
}

internal fun CustomTabsIntent.setCustomTabsPackage(
    context: Context,
    customTabsPackages: List<String>,
    fallback: CustomTabsPackageFallback? = null,
) {
    val customTabsPackage =
        CustomTabsClient.getPackageName(context, customTabsPackages, true)
    if (customTabsPackage == null && fallback != null) {
        with(fallback) {
            setCustomTabsPackage(context)
        }
        return
    }
    intent.setPackage(customTabsPackage)
}