package com.droibit.android.customtabs.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.customtabs.CustomTabsIntent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;
import static android.content.pm.PackageManager.GET_META_DATA;

/**
 * @author kumagai
 */
@VisibleForTesting
class CustomTabsLauncherImpl {

    static final String PACKAGE_STABLE = "com.android.chrome";
    static final String PACKAGE_BETA = "com.chrome.beta";
    static final String PACKAGE_DEV = "com.chrome.dev";
    static final String PACKAGE_LOCAL = "com.google.android.apps.chrome";
    static final List<String> CHROME_PACKAGES = Arrays.asList(
            PACKAGE_STABLE,
            PACKAGE_BETA,
            PACKAGE_DEV,
            PACKAGE_LOCAL);

     static final String ACTION_CUSTOM_TABS_CONNECTION =
            "android.support.customtabs.action.CustomTabsService";

    @VisibleForTesting
    void launchUrl(@NonNull Activity activity,
                   @NonNull CustomTabsIntent customTabsIntent,
                   @NonNull Uri uri,
                   @Nullable CustomTabsFallback fallback) {

        final String chromePackage = packageNameToUse(activity, uri);
        if (chromePackage == null && fallback != null) {
            fallback.openUri(activity, uri);
            return;
        }

        customTabsIntent.intent.setPackage(chromePackage);
        customTabsIntent.launchUrl(activity, uri);
    }

    @Nullable
    @VisibleForTesting
    String packageNameToUse(Context context, Uri uri) {
        final PackageManager pm = context.getPackageManager();

        // Get default VIEW intent handler.
        final Intent activityIntent = new Intent(ACTION_VIEW, uri);
        final ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);
        // If Chrome is default browser, use it.
        if (defaultViewHandlerInfo != null) {
            final String defaultPackageName = defaultViewHandlerInfo.activityInfo.packageName;
            if (CHROME_PACKAGES.contains(defaultPackageName) &&
                    supportedCustomTabs(pm, defaultPackageName)) {
                return defaultPackageName;
            }
        }

        final List<ApplicationInfo> installedApps = pm.getInstalledApplications(GET_META_DATA);
        final List<String> installedChromes = new ArrayList<>(CHROME_PACKAGES.size());
        for (ApplicationInfo app : installedApps) {
            if (CHROME_PACKAGES.contains(app.packageName)) {
                installedChromes.add(app.packageName);
            }
        }

        if (installedChromes.isEmpty()) {
            return null;
        }

        // Stable comes first.
        for (String chromePackage : CHROME_PACKAGES) {
            if (installedChromes.contains(chromePackage) &&
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
