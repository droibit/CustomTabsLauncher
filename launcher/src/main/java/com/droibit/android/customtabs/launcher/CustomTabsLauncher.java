package com.droibit.android.customtabs.launcher;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;

/**
 * @author kumagai
 */
public final class CustomTabsLauncher {

    private static final CustomTabsLauncherImpl IMPL = new CustomTabsLauncherImpl();

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
