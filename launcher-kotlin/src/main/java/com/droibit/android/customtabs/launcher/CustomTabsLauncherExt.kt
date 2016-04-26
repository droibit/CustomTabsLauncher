package com.droibit.android.customtabs.launcher

import android.app.Activity
import android.net.Uri
import android.support.customtabs.CustomTabsIntent

/**
 * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView.
 *
 * @param activity The host activity.
 * @param uri the Uri to be opened.
 * @param fallback a [CustomTabsFallback] to be used if Custom Tabs is not available.
*/
fun CustomTabsIntent.launchUrl(activity: Activity, uri: Uri, fallback: CustomTabsFallback? = null) {
    CustomTabsLauncher.launchUrl(activity, this, uri, fallback)
}
