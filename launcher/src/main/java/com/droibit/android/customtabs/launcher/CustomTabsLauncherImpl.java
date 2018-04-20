package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.customtabs.CustomTabsIntent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;
import static android.support.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY) class CustomTabsLauncherImpl {

  @VisibleForTesting static final String PACKAGE_STABLE = "com.android.chrome";
  @VisibleForTesting static final String PACKAGE_BETA = "com.chrome.beta";
  @VisibleForTesting static final String PACKAGE_DEV = "com.chrome.dev";
  @VisibleForTesting static final String PACKAGE_LOCAL = "com.google.android.apps.chrome";

  private static final List<String> CHROME_PACKAGES =
      Arrays.asList(PACKAGE_STABLE, PACKAGE_BETA, PACKAGE_DEV, PACKAGE_LOCAL);

  private static final String ACTION_CUSTOM_TABS_CONNECTION =
      "android.support.customtabs.action.CustomTabsService";

  boolean canLaunch(@NonNull Context context, @NonNull Uri uri) {
    return getPackageNameToUse(context.getPackageManager(), uri) != null;
  }

  void launch(@NonNull Context context, @NonNull CustomTabsIntent customTabsIntent,
      @NonNull Uri uri, @Nullable CustomTabsFallback fallback) {

    final PackageManager pm = context.getPackageManager();
    final String chromePackage = getPackageNameToUse(pm, uri);
    if (chromePackage == null && fallback != null) {
      fallback.openUrl(context, uri);
      return;
    }

    customTabsIntent.intent.setPackage(chromePackage);
    customTabsIntent.launchUrl(context, uri);
  }

  @VisibleForTesting @Nullable String getPackageNameToUse(@NonNull PackageManager pm,
      @NonNull Uri uri) {
    final String defaultPackageName = getDefaultViewHandlerPackageName(pm, uri);
    // If Chrome is default browser, use it.
    if (defaultPackageName != null) {
      if (CHROME_PACKAGES.contains(defaultPackageName) && supportedCustomTabs(pm,
          defaultPackageName)) {
        return defaultPackageName;
      }
    }

    final List<String> chromePackages = getInstalledChromePackageNames(pm, uri);
    if (chromePackages.isEmpty()) {
      return null;
    }

    // Stable comes first.
    return decidePackage(pm, chromePackages);
  }

  @VisibleForTesting @Nullable String getDefaultViewHandlerPackageName(@NonNull PackageManager pm,
      @NonNull Uri uri) {
    // Get default VIEW intent handler.
    final Intent activityIntent = new Intent(ACTION_VIEW, uri);
    final ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
    if (defaultViewHandlerInfo != null) {
      return defaultViewHandlerInfo.activityInfo.packageName;
    }
    return null;
  }

  @VisibleForTesting @NonNull List<String> getInstalledChromePackageNames(@NonNull PackageManager pm, @NonNull Uri uri) {
    final int flag;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      flag = PackageManager.MATCH_ALL;
    } else {
      flag = PackageManager.MATCH_DEFAULT_ONLY;
    }
    final Intent activityIntent = new Intent(ACTION_VIEW, uri);
    final List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(activityIntent, flag);

    final List<String> installedChromes = new ArrayList<>(CHROME_PACKAGES.size());
    for (ResolveInfo resolveInfo : resolveInfoList) {
      final String packageName = resolveInfo.activityInfo.packageName;
      if (CHROME_PACKAGES.contains(packageName)) {
        installedChromes.add(packageName);
      }
    }
    return installedChromes;
  }

  @VisibleForTesting @Nullable String decidePackage(@NonNull PackageManager pm,
      @NonNull List<String> candidates) {
    for (String chromePackage : CHROME_PACKAGES) {
      if (candidates.contains(chromePackage) && supportedCustomTabs(pm, chromePackage)) {
        return chromePackage;
      }
    }
    return null;
  }

  @VisibleForTesting boolean supportedCustomTabs(@NonNull PackageManager pm,
      @NonNull String chromePackage) {
    // Whether support Chrome Custom Tabs.
    final Intent serviceIntent =
        new Intent(ACTION_CUSTOM_TABS_CONNECTION).setPackage(chromePackage);
    return pm.resolveService(serviceIntent, 0) != null;
  }
}
