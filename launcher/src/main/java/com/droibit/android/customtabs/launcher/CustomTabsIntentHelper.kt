@file:JvmName("CustomTabsIntentHelper")

package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import com.droibit.android.customtabs.launcher.internal.CustomTabsPackage.CHROME_PACKAGES

/**
 * Sets the package name of Chrome to the [CustomTabsIntent] explicitly to launch it as a Custom Tab.
 *
 * The browser priorities are as follows:
 * 1. [Chrome](https://play.google.com/store/apps/details?id=com.android.chrome).
 * 2. [Chrome Beta](https://play.google.com/store/apps/details?id=com.chrome.beta).
 * 3. [Chrome Dev](https://play.google.com/store/apps/details?id=com.chrome.dev).
 * 4. Local(com.google.android.apps.chrome).
 * 5. (Optional) Browsers provided by [CustomTabsPackageFallback].
 *
 * ## Usage
 * - Basic usage:
 * ```
 * val customTabsIntent = build()
 * customTabsIntent.ensureChromeCustomTabsPackage(context)
 * customTabsIntent.launchUrl(context, Uri)
 * ```
 *
 * - Present the custom tab as bottom sheet:
 * ```
 * val activityLauncher = registerForActivityResult(StartActivityForResult()) {
 *      // Do something.
 * }
 *
 * val customTabsIntent = build().apply {
 *      ensureChromeCustomTabsPackage(context)
 *      intent.data = Uri
 * }
 * activityLauncher.launch(customTabsIntent.intent)
 * ```
 *
 * @param context The source Context
 * @param fallback A [CustomTabsPackageFallback] to be used if Chrome is not available.
 */
@JvmOverloads
fun CustomTabsIntent.ensureChromeCustomTabsPackage(
    context: Context,
    fallback: CustomTabsPackageFallback? = null,
): CustomTabsIntent {
    setCustomTabsPackage(context, CHROME_PACKAGES, true, fallback)
    return this
}

/**
 * Explicitly sets the package name of the browser that supports Custom Tabs
 * to the [CustomTabsIntent] to launch it as a Custom Tab.
 *
 * The browser priorities are as follows:
 * 1. The default browser that supports Custom Tabs.
 * 2. [Chrome](https://play.google.com/store/apps/details?id=com.android.chrome).
 * 3. [Chrome Beta](https://play.google.com/store/apps/details?id=com.chrome.beta).
 * 4. [Chrome Dev](https://play.google.com/store/apps/details?id=com.chrome.dev).
 * 5. Local(com.google.android.apps.chrome).
 * 6. (Optional) Browsers provided by [CustomTabsPackageFallback].
 *
 * ## Usage
 * - Basic usage:
 * ```
 * val customTabsIntent = build()
 * customTabsIntent.ensureCustomTabsPackage(context)
 * customTabsIntent.launchUrl(context, Uri)
 * ```
 *
 * - Present the custom tab as bottom sheet:
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
 * @param fallback A [CustomTabsPackageFallback] to be used if the default browser or Chrome are not available.
 */
@JvmOverloads
fun CustomTabsIntent.ensureCustomTabsPackage(
    context: Context,
    fallback: CustomTabsPackageFallback? = null,
): CustomTabsIntent {
    setCustomTabsPackage(context, CHROME_PACKAGES, false, fallback)
    return this
}

internal fun CustomTabsIntent.setCustomTabsPackage(
    context: Context,
    customTabsPackages: List<String>,
    ignoreDefault: Boolean = true,
    fallback: CustomTabsPackageFallback? = null,
) {
    val customTabsPackage =
        CustomTabsClient.getPackageName(context, customTabsPackages, ignoreDefault)
    if (customTabsPackage == null && fallback != null) {
        with(fallback) {
            setCustomTabsPackage(context)
        }
        return
    }
    intent.setPackage(customTabsPackage)
}