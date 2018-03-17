package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * To be used as a fallback to open the Uri when Custom Tabs is not available.
 */
public interface CustomTabsFallback {

  /**
   * @param context The source Context
   * @param url The URL to load in the Custom Tab
   */
  void openUrl(@NonNull Context context, @NonNull Uri url);
}