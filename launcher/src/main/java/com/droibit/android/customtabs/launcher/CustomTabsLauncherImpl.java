package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import java.util.ArrayList;
import java.util.List;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY) class CustomTabsLauncherImpl {

  void launch(@NonNull Context context, @NonNull CustomTabsIntent customTabsIntent,
      @NonNull Uri uri, @NonNull List<String> expectCustomTabsPackages,
      @Nullable CustomTabsFallback fallback) {
    final List<String> customTabsPackages = new ArrayList<>(ChromePackage.CHROME_PACKAGES);
    customTabsPackages.addAll(expectCustomTabsPackages);
    final String customTabsPackage = CustomTabsClient.getPackageName(context, customTabsPackages);
    if (customTabsPackage == null && fallback != null) {
      fallback.openUrl(context, uri, customTabsIntent);
      return;
    }

    customTabsIntent.intent.setPackage(customTabsPackage);
    customTabsIntent.launchUrl(context, uri);
  }
}
