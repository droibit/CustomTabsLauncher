package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.customtabs.CustomTabsIntent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;
import static android.content.pm.PackageManager.GET_META_DATA;
import static android.support.annotation.RestrictTo.Scope.LIBRARY;
import static android.support.annotation.VisibleForTesting.PACKAGE_PRIVATE;


@RestrictTo(LIBRARY)
class CustomTabsLauncherImpl {

    @VisibleForTesting
    static final String PACKAGE_STABLE = "com.android.chrome";
    @VisibleForTesting
    static final String PACKAGE_BETA = "com.chrome.beta";
    @VisibleForTesting
    static final String PACKAGE_DEV = "com.chrome.dev";
    @VisibleForTesting
    static final String PACKAGE_LOCAL = "com.google.android.apps.chrome";

    private static final List<String> CHROME_PACKAGES = Arrays.asList(
            PACKAGE_STABLE,
            PACKAGE_BETA,
            PACKAGE_DEV,
            PACKAGE_LOCAL);

    private static final String ACTION_CUSTOM_TABS_CONNECTION =
            "android.support.customtabs.action.CustomTabsService";

    @VisibleForTesting(otherwise = PACKAGE_PRIVATE)
    void launch(@NonNull Context context,
                @NonNull CustomTabsIntent customTabsIntent,
                @NonNull Uri uri,
                @Nullable CustomTabsFallback fallback) {

        final PackageManager pm = context.getPackageManager();
        final String chromePackage = packageNameToUse(pm, uri);
        if (chromePackage == null && fallback != null) {
            fallback.openUrl(context, uri);
            return;
        }

        customTabsIntent.intent.setPackage(chromePackage);
        customTabsIntent.launchUrl(context, uri);
    }

    @Nullable
    @VisibleForTesting
    String packageNameToUse(PackageManager pm, Uri uri) {
        final String defaultPackageName = defaultViewHandlerPackage(pm, uri);
        // If Chrome is default browser, use it.
        if (defaultPackageName != null) {
            if (CHROME_PACKAGES.contains(defaultPackageName) &&
                    supportedCustomTabs(pm, defaultPackageName)) {
                return defaultPackageName;
            }
        }

        final List<String> chromePackages = installedPackages(pm);
        if (chromePackages.isEmpty()) {
            return null;
        }

        // Stable comes first.
        return decidePackage(pm, chromePackages);
    }

    @Nullable
    @VisibleForTesting
    String defaultViewHandlerPackage(PackageManager pm, Uri uri) {
        // Get default VIEW intent handler.
        final Intent activityIntent = new Intent(ACTION_VIEW, uri);
        final ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
        if (defaultViewHandlerInfo != null) {
            return defaultViewHandlerInfo.activityInfo.packageName;
        }
        return null;
    }

    @NonNull
    @VisibleForTesting
    List<String> installedPackages(PackageManager pm) {
        final List<ApplicationInfo> installedApps = pm.getInstalledApplications(GET_META_DATA);
        final List<String> installedChromes = new ArrayList<>(CHROME_PACKAGES.size());
        for (ApplicationInfo app : installedApps) {
            if (CHROME_PACKAGES.contains(app.packageName)) {
                installedChromes.add(app.packageName);
            }
        }
        return installedChromes;
    }

    @VisibleForTesting
    String decidePackage(PackageManager pm, List<String> candidates) {
        for (String chromePackage : CHROME_PACKAGES) {
            if (candidates.contains(chromePackage) &&
                    supportedCustomTabs(pm, chromePackage)) {
                return chromePackage;
            }
        }
        return null;
    }

    @VisibleForTesting
    boolean supportedCustomTabs(PackageManager pm, String chromePackage) {
        // Whether support Chrome Custom Tabs.
        final Intent serviceIntent = new Intent(ACTION_CUSTOM_TABS_CONNECTION)
                .setPackage(chromePackage);
        return pm.resolveService(serviceIntent, 0) != null;
    }
}
