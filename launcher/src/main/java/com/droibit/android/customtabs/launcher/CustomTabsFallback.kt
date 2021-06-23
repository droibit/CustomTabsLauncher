package com.droibit.android.customtabs.launcher

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

/**
 * To be used as a fallback to open the Uri when Custom Tabs is not available.
 */
fun interface CustomTabsFallback {
    /**
     * @param context          The source Context
     * @param uri              The Uri to load in the Custom Tab
     * @param customTabsIntent a source CustomTabsIntent
     */
    fun openUrl(context: Context, uri: Uri, customTabsIntent: CustomTabsIntent)
}