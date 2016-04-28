package com.droibit.android.customtabs.launcher;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_BETA;
import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_DEV;
import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_LOCAL;
import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_STABLE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author kumagai
 */
public class CustomTabsLauncherImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Spy
    CustomTabsLauncherImpl launcher;

    // TODO: CustomTabsIntent is final calss.
    public void launchSuccess() {

    }

    @Test
    public void launchFailed() {
        final Uri uri = mock(Uri.class);

        final Activity activity = mock(Activity.class);
        doReturn(mock(PackageManager.class))
                .when(activity)
                .getPackageManager();

        final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
        doReturn(null)
                .when(launcher)
                .packageNameToUse(any(PackageManager.class), any(Uri.class));

        //noinspection ConstantConditions
        launcher.launch(activity, null, uri, fallback);
        verify(fallback).openUri(any(Activity.class), any(Uri.class));
    }

    @Test
    public void decideStableChrome() {
        doReturn(true)
                .when(launcher)
                .supportedCustomTabs(any(PackageManager.class), anyString());

        final List<String> candidates = singletonList(PACKAGE_STABLE);
        final String stable = launcher.chromePackageName(candidates, null);
        assertThat(stable, equalTo(PACKAGE_STABLE));
    }

    @Test
    public void decideStableChromeFromMultipleVersions() {
        doReturn(true)
                .when(launcher)
                .supportedCustomTabs(any(PackageManager.class), anyString());

        final List<String> candidates = asList(
                PACKAGE_LOCAL,
                PACKAGE_DEV,
                PACKAGE_BETA,
                PACKAGE_STABLE);
        final String stable = launcher.chromePackageName(candidates, null);
        assertThat(stable, equalTo(PACKAGE_STABLE));
    }

    @Test
    public void decideBetaChrome() {
        doReturn(false)
                .when(launcher)
                .supportedCustomTabs(any(PackageManager.class), eq(PACKAGE_STABLE));
        doReturn(true)
                .when(launcher)
                .supportedCustomTabs(any(PackageManager.class), eq(PACKAGE_BETA));

        final List<String> candidates = asList(
                PACKAGE_BETA,
                PACKAGE_STABLE);
        final String beta = launcher.chromePackageName(candidates, null);
        assertThat(beta, equalTo(PACKAGE_BETA));
    }

    @Test
    public void decideDevChromePackage() {
        // Chrome Dev the default browser.
        doReturn(PACKAGE_DEV)
                .when(launcher)
                .defaultViewHandlerPackage(any(PackageManager.class), any(Uri.class));
        doReturn(asList(PACKAGE_STABLE, PACKAGE_BETA,  PACKAGE_DEV))
                .when(launcher)
                .installedChromes(any(PackageManager.class));
        doReturn(true)
                .when(launcher)
                .supportedCustomTabs(any(PackageManager.class), anyString());

        final String dev = launcher.packageNameToUse(any(PackageManager.class), any(Uri.class));
        assertThat(dev, equalTo(PACKAGE_DEV));
    }
}