package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

/**
 * To be used as a fallback to open the Uri when Custom Tabs is not available.
 */
public interface CustomTabsFallback {

  /**
   * @param context The source Context
   * @param uri The Uri to load in the Custom Tab
   * @param customTabsIntent a source CustomTabsIntent
   */
  void openUrl(@NonNull Context context, @NonNull Uri uri, @NonNull CustomTabsIntent customTabsIntent);
}