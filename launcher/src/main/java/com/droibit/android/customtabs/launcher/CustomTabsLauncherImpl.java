package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.VisibleForTesting;
import androidx.browser.customtabs.CustomTabsIntent;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY) class CustomTabsLauncherImpl {

  private static final String ACTION_CUSTOM_TABS_CONNECTION =
      "android.support.customtabs.action.CustomTabsService";

  void launch(
      @NonNull Context context,
      @NonNull CustomTabsIntent customTabsIntent,
      @NonNull Uri uri,
      @NonNull List<String> expectCustomTabsPackages,
      @Nullable CustomTabsFallback fallback) {

    final PackageManager pm = context.getPackageManager();
    final String customTabsPackage = getPackageNameToUse(pm, expectCustomTabsPackages, uri);
    if (customTabsPackage == null && fallback != null) {
      fallback.openUrl(context, uri, customTabsIntent);
      return;
    }

    customTabsIntent.intent.setPackage(customTabsPackage);
    customTabsIntent.launchUrl(context, uri);
  }

  @VisibleForTesting @Nullable String getPackageNameToUse(
      @NonNull PackageManager pm,
      @NonNull List<String> expectCustomTabsPackages,
      @NonNull Uri uri) {
    final String defaultPackageName = getDefaultViewHandlerPackageName(pm, uri);
    if (defaultPackageName != null) {
      if (expectCustomTabsPackages.contains(defaultPackageName)
          && supportedCustomTabs(pm, defaultPackageName)) {
        return defaultPackageName;
      }
    }

    final List<String> installedCustomTabsPackages =
        getInstalledCustomTabsPackageNames(pm, expectCustomTabsPackages, uri);
    if (installedCustomTabsPackages.isEmpty()) {
      return null;
    }
    return decidePackage(expectCustomTabsPackages, installedCustomTabsPackages);
  }

  @Nullable
  private String getDefaultViewHandlerPackageName(
      @NonNull PackageManager pm,
      @NonNull Uri uri) {
    // Get default VIEW intent handler.
    final Intent activityIntent = new Intent(ACTION_VIEW, uri);
    final ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
    if (defaultViewHandlerInfo != null) {
      return defaultViewHandlerInfo.activityInfo.packageName;
    }
    return null;
  }

  @NonNull
  private List<String> getInstalledCustomTabsPackageNames(
      @NonNull PackageManager pm,
      @NonNull List<String> expectCustomTabsPackages,
      @NonNull Uri uri) {
    final int flag;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      flag = PackageManager.MATCH_ALL;
    } else {
      flag = PackageManager.MATCH_DEFAULT_ONLY;
    }
    final Intent activityIntent = new Intent(ACTION_VIEW, uri);
    final List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(activityIntent, flag);

    final List<String> installedCustomTabs = new ArrayList<>(expectCustomTabsPackages.size());
    for (ResolveInfo resolveInfo : resolveInfoList) {
      final String packageName = resolveInfo.activityInfo.packageName;
      if (supportedCustomTabs(pm, packageName)) {
        installedCustomTabs.add(packageName);
      }
    }
    return installedCustomTabs;
  }

  @Nullable
  private String decidePackage(
      @NonNull List<String> expectCustomTabsPackage,
      @NonNull List<String> installedCustomTabsPackages) {
    for (String packageName : expectCustomTabsPackage) {
      if (installedCustomTabsPackages.contains(packageName) ) {
        return packageName;
      }
    }
    return null;
  }

  boolean supportedCustomTabs(
      @NonNull PackageManager pm,
      @NonNull String packageName) {
    // Whether support Chrome Custom Tabs.
    final Intent serviceIntent =
        new Intent(ACTION_CUSTOM_TABS_CONNECTION).setPackage(packageName);
    return pm.resolveService(serviceIntent, 0) != null;
  }
}
