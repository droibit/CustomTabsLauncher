package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;

/**
 * Class to launch with priority Chrome Custom Tabs.<br/>
 * <p>
 * The default browser is not the Chrome, and more than one browser is installed,
 * this time Chrome Custom Tabs does not start to direct.<br/>
 * By using this class, Chrome is to launch with priority if it is installed.
 * </p>
 */
public final class CustomTabsLauncher {

  private static final CustomTabsLauncherImpl IMPL = new CustomTabsLauncherImpl();

  /**
   * Whether URL can be opened with Custom Tabs
   *
   * @param context The source Context
   * @param uriString the Uri to be opened
   * @return Can be opened if {@code true}, can not be {@code false}.
   */
  public static boolean canLaunch(@NonNull Context context, @NonNull String uriString) {
    return canLaunch(context, Uri.parse(uriString));
  }

  /**
   * Whether URL can be opened with Custom Tabs
   *
   * @param context The source Context
   * @param uri the Uri to be opened
   * @return Can be opened if {@code true}, can not be {@code false}.
   */
  public static boolean canLaunch(@NonNull Context context, @NonNull Uri uri) {
    return IMPL.canLaunch(context, uri);
  }

  /**
   * Opens the URL on a Custom Tabs if possible.
   *
   * @param context The source Context
   * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
   * @param uriString the Uri to be opened
   */
  public static void launch(@NonNull Context context, @NonNull CustomTabsIntent customTabsIntent,
      @NonNull String uriString) {
    launch(context, customTabsIntent, Uri.parse(uriString), null);
  }

  /**
   * Opens the URL on a Custom Tabs if possible. Otherwise fallsback to opening it on a WebView.
   *
   * @param context The source Context
   * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
   * @param uriString the Uri to be opened
   * @param fallback a {@link CustomTabsFallback} to be used if Custom Tabs is not available
   */
  public static void launch(@NonNull Context context, @NonNull CustomTabsIntent customTabsIntent,
      @NonNull String uriString, @Nullable CustomTabsFallback fallback) {
    launch(context, customTabsIntent, Uri.parse(uriString), fallback);
  }

  /**
   * Opens the URL on a Custom Tabs if possible.
   *
   * @param context The source Context
   * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
   * @param uri the Uri to be opened.
   */
  public static void launch(@NonNull Context context, @NonNull CustomTabsIntent customTabsIntent,
      @NonNull Uri uri) {
    launch(context, customTabsIntent, uri, null);
  }

  /**
   * Opens the URL on a Custom Tabs if possible. Otherwise fallsback to opening it on a WebView.
   *
   * @param context The source Context
   * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
   * @param uri the Uri to be opened
   * @param fallback a {@link CustomTabsFallback} to be used if Custom Tabs is not available.
   */
  public static void launch(
      @NonNull Context context,
      @NonNull CustomTabsIntent customTabsIntent,
      @NonNull Uri uri,
      @Nullable CustomTabsFallback fallback) {
    IMPL.launch(context, customTabsIntent, uri, fallback);
  }
}
