@file:JvmName("AuthTabIntentHelper")

package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.auth.AuthTabIntent

/**
 * Sets the package name of Chrome or an alternative browser that supports the [Auth Tab](https://developer.chrome.com/docs/android/custom-tabs/guide-auth-tab)
 * to the [AuthTabIntent] explicitly for launching URLs as the Auth Tab.
 *
 * ## Browser Priorities
 * 1. [Chrome](https://play.google.com/store/apps/details?id=com.android.chrome).
 * 2. [Chrome Beta](https://play.google.com/store/apps/details?id=com.chrome.beta).
 * 3. [Chrome Dev](https://play.google.com/store/apps/details?id=com.chrome.dev).
 * 4. Local(com.google.android.apps.chrome).
 * 5. (Optional) Browsers provided by [CustomTabsPackageProvider].
 *
 * ## Usage
 *
 * ```
 * val launcher = AuthTabIntent.registerActivityResultLauncher(context) { result ->
 *   // Handle the result of the Auth Tab.
 * }
 *
 * val authTabIntent = buildAuthTabIntent()
 *   .setChromeCustomTabsPackage(context)
 * authTabIntent.launch(launcher, AUTHENTICATION_URL, REDIRECT_SCHEME)
 * ```
 *
 * @param context The source Context
 * @param additionalAuthTabs A [CustomTabsPackageProvider] providing additional browser packages that support the Auth Tab.
 *
 * @return The modified [AuthTabIntent] with the specified package set.
 */
@JvmOverloads
fun AuthTabIntent.setChromeCustomTabsPackage(
  context: Context,
  additionalAuthTabs: CustomTabsPackageProvider? = null,
): AuthTabIntent {
  // TODO: Check if stable Chrome can be correctly detected by `CustomTabsClient.isAuthTabSupported` for feature availability.
  getCustomTabsPackage(context, true, additionalAuthTabs)?.let { packageName ->
    intent.`package` = packageName
  }
  return this
}
