package com.droibit.android.customtabs.launcher

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

/**
 * Attempt to open the URL directly in a browser that supports Custom Tabs.
 *
 * @param context The source Context
 * @param url the Uri to be opened
 * @param fallback a [CustomTabsFallback] to be used if Custom Tabs is not available.
 */
fun CustomTabsIntent.launch(
    context: Context,
    url: String,
    fallback: CustomTabsFallback? = null
) {
    CustomTabsLauncher.launch(context, this, Uri.parse(url), fallback)
}

/**
 * Attempt to open the URL directly in a browser that supports Custom Tabs.
 *
 * @param context The source Context
 * @param url the [Uri] to be opened
 * @param fallback a [CustomTabsFallback] to be used if Custom Tabs is not available.
 */
fun CustomTabsIntent.launch(
    context: Context,
    url: Uri,
    fallback: CustomTabsFallback? = null
) {
    CustomTabsLauncher.launch(context, this, url, fallback)
}