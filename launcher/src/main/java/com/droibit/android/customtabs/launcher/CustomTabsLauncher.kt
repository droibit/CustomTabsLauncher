package com.droibit.android.customtabs.launcher

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.droibit.android.customtabs.launcher.CustomTabsLauncher.LaunchNonChromeCustomTabs
import com.droibit.android.customtabs.launcher.internal.CustomTabsLauncherImpl
import com.droibit.android.customtabs.launcher.internal.CustomTabsPackage
import com.droibit.android.customtabs.launcher.internal.CustomTabsPackage.CHROME_PACKAGES

/**
 * A utility class that directly launches a browser that supports Custom Tabs.
 *
 * This library try to launch Chrome(s) as the default behavior,
 * but if they are not installed you can launch a browser that supports other Custom Tabs using [LaunchNonChromeCustomTabs].
 *
 * * Basic usage:
 * ```
 * val customTabsIntent = ...;
 * CustomTabsLauncher.launch(ctx, customTabsIntent, URI);
 * ```
 *
 * * Launch non-Chrome browser as Fallback
 * ```
 * val customTabsIntent = ...;
 * CustomTabsLauncher.launch(ctx, customTabsIntent, URI, LaunchNonChromeCustomTabs(ctx));
 * ```
 */
object CustomTabsLauncher {
    private val IMPL = CustomTabsLauncherImpl()

    /**
     * Attempt to open the URL directly in a browser that supports Custom Tabs.
     *
     * @param context          The source Context
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
     * @param uri              the Uri to be opened.
     * @param fallback         a [CustomTabsFallback] to be used if Custom Tabs is not available.
     */
    @JvmStatic
    @JvmOverloads
    fun launch(
        context: Context,
        customTabsIntent: CustomTabsIntent,
        uri: Uri,
        fallback: CustomTabsFallback? = null
    ) {
        IMPL.launch(context, customTabsIntent, uri, CHROME_PACKAGES, fallback)
    }

    /**
     * Fallback for launch a browser installed on the device.
     */
    class LaunchBrowser : CustomTabsFallback {
        override fun openUrl(
            context: Context,
            uri: Uri,
            customTabsIntent: CustomTabsIntent
        ) {
            val intent = Intent(Intent.ACTION_VIEW, uri)
                .setFlags(customTabsIntent.intent.flags)
            context.startActivity(intent)
        }
    }

    /**
     * Fallback for launch a non-Chrome browser that supports Custom Tabs.
     *
     * @param customTabsPackages Package list of non-Chrome browsers supporting Custom Tabs. The top of the list is used with the highest priority.
     */
    class LaunchNonChromeCustomTabs(
        private val customTabsPackages: List<String>
    ) : CustomTabsFallback {

        constructor(context: Context) : this(
            CustomTabsPackage.getNonChromeCustomTabsPackages(context)
        )

        override fun openUrl(
            context: Context,
            uri: Uri,
            customTabsIntent: CustomTabsIntent
        ) {
            if (customTabsPackages.isEmpty()) {
                customTabsIntent.launchUrl(context, uri)
            } else {
                IMPL.launch(context, customTabsIntent, uri, customTabsPackages, null)
            }
        }
    }
}