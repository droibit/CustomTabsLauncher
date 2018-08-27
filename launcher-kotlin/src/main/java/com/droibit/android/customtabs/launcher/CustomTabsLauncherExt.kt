package com.droibit.android.customtabs.launcher

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent

/**
 * Opens the URL on a Custom Tab if possible. Otherwise fallback to opening it on a WebView.
 *
 * @param context The source Context
 * @param url the Uri to be opened
 * @param fallback a [CustomTabsFallback] to be used if Custom Tabs is not available.
 */
fun CustomTabsIntent.launch(
  context: Context,
  url: String,
  fallback: ((Context, Uri, CustomTabsIntent) -> Unit)? = null
) {
  CustomTabsLauncher.launch(context, this, Uri.parse(url), fallback)
}

/**
 * Opens the URL on a Custom Tab if possible. Otherwise fallback to opening it on a WebView.
 *
 * @param context The source Context
 * @param url the [Uri] to be opened
 * @param fallback a [CustomTabsFallback] to be used if Custom Tabs is not available.
 */
fun CustomTabsIntent.launch(
  context: Context,
  url: Uri,
  fallback: ((Context, Uri, CustomTabsIntent) -> Unit)? = null
) {
  CustomTabsLauncher.launch(context, this, url, fallback)
}