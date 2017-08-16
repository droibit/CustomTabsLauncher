package com.droibit.android.customtabs.launcher;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_BETA;
import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_DEV;
import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_LOCAL;
import static com.droibit.android.customtabs.launcher.CustomTabsLauncherImpl.PACKAGE_STABLE;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomTabsLauncherImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private PackageManager pm;

    @Mock
    private Uri uri;

    @Spy
    private CustomTabsLauncherImpl launcher;

    public void launchSuccess() {
        // TODO: CustomTabsIntent is final calss.
    }

    @Test
    public void launch_launchFailed() {
        final Activity activity = mock(Activity.class);
        when(activity.getPackageManager()).thenReturn(mock(PackageManager.class));

        final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
        doReturn(null).when(launcher).packageNameToUse(any(PackageManager.class), any(Uri.class));

        //noinspection ConstantConditions
        launcher.launch(activity, null, uri, fallback);
        verify(fallback).openUrl(any(Activity.class), any(Uri.class));
    }

    @Test
    public void packageNameToUse_useChromeAsDefault() throws Exception {
        doReturn(PACKAGE_LOCAL).when(launcher)
                .defaultViewHandlerPackage(any(PackageManager.class), any(Uri.class));
        doReturn(true)
                .when(launcher)
                .supportedCustomTabs(any(PackageManager.class), anyString());

        final String chrome = launcher.packageNameToUse(pm, uri);
        assertThat(chrome, equalTo(PACKAGE_LOCAL));
    }

    @Test
    public void packageNameToUse_notInstalledChrome() throws Exception {
        doReturn(null).when(launcher)
                .defaultViewHandlerPackage(any(PackageManager.class), any(Uri.class));
        doReturn(Collections.emptyList()).when(launcher)
                .installedPackages(any(PackageManager.class));

        final String packageName = launcher.packageNameToUse(pm, uri);
        assertThat(packageName, is(nullValue()));
    }

    @Test
    public void packageNameToUse_prioritizeStableChrome() throws Exception {
        doReturn(null).when(launcher)
                .defaultViewHandlerPackage(any(PackageManager.class), any(Uri.class));

        final List<String> candidates = asList(
                PACKAGE_LOCAL,
                PACKAGE_DEV,
                PACKAGE_BETA,
                PACKAGE_STABLE
        );
        doReturn(candidates).when(launcher)
                .installedPackages(any(PackageManager.class));
        doReturn(true).when(launcher)
                .supportedCustomTabs(any(PackageManager.class), anyString());

        final String stable = launcher.decidePackage(pm, candidates);
        assertThat(stable, equalTo(PACKAGE_STABLE));
    }
}