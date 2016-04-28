package com.droibit.android.customtabs.launcher;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;

/**
 * Class to launch with priority Chrome Custom Tabs.
 *
 * The default browser is not the Chrome, and more than one browser is installed,
 * this time Chrome Custom Tabs does not start to direct.
 * By using this class, Chrome is to launch with priority if it is installed.
 *
 * @author kumagai
 */
public final class CustomTabsLauncher {

    private static final CustomTabsLauncherImpl IMPL = new CustomTabsLauncherImpl();

    /**
     * Opens the URL on a Custom Tab if possible.
     *
     * @param activity The host activity.
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available.
     * @param uriString the Uri to be opened.
     */
    public static void launch(@NonNull Activity activity,
                              @NonNull CustomTabsIntent customTabsIntent,
                              @NonNull String uriString) {
        launch(activity, customTabsIntent, Uri.parse(uriString), null);
    }

    /**
     * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView.
     *
     * @param activity The host activity.
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available.
     * @param uriString the Uri to be opened.
     * @param fallback a {@link CustomTabsFallback} to be used if Custom Tabs is not available.
     */
    public static void launch(@NonNull Activity activity,
                              @NonNull CustomTabsIntent customTabsIntent,
                              @NonNull String uriString,
                              @Nullable CustomTabsFallback fallback) {
        launch(activity, customTabsIntent, Uri.parse(uriString), fallback);
    }

    /**
     * Opens the URL on a Custom Tab if possible.
     *
     * @param activity The host activity.
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available.
     * @param uri the Uri to be opened.
     */
    public static void launch(@NonNull Activity activity,
                              @NonNull CustomTabsIntent customTabsIntent,
                              @NonNull Uri uri) {
        launch(activity, customTabsIntent, uri, null);
    }

    /**
     * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView.
     *
     * @param activity The host activity.
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available.
     * @param uri the Uri to be opened.
     * @param fallback a {@link CustomTabsFallback} to be used if Custom Tabs is not available.
     */
    public static void launch(@NonNull Activity activity,
                              @NonNull CustomTabsIntent customTabsIntent,
                              @NonNull Uri uri,
                              @Nullable CustomTabsFallback fallback) {
        IMPL.launch(activity, customTabsIntent, uri, fallback);
    }
}
