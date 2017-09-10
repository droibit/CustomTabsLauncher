package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 16)
public class CustomTabsLauncherImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private PackageManager pm;

    @Mock
    private Uri uri;

    @Spy
    private CustomTabsLauncherImpl launcher;

    @Test
    public void launch_launchSuccess() {
        final Context context = when(mock(Context.class).getPackageManager())
                .thenReturn(pm).getMock();

        final String packageName = "package.name.test";
        doReturn(packageName)
                .when(launcher)
                .packageNameToUse(((PackageManager) any()), ((Uri) any()));

        final CustomTabsIntent customTabsIntent = spy(new CustomTabsIntent.Builder().build());
        final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
        //noinspection ConstantConditions
        launcher.launch(context, customTabsIntent, uri, fallback);

        verify(customTabsIntent).launchUrl(((Context) any()), same(uri));
        verify(fallback, never()).openUrl((Context) any(), (Uri) any());
    }

    @Test
    public void launch_launchFailed() {
        final Context context = when(mock(Context.class).getPackageManager())
                .thenReturn(pm).getMock();

        final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
        doReturn(null)
                .when(launcher)
                .packageNameToUse(((PackageManager) any()), ((Uri) any()));

        //noinspection ConstantConditions
        launcher.launch(context, null, uri, fallback);
        verify(fallback).openUrl((Context) any(), same(uri));
    }

    @Test
    public void packageNameToUse_useChromeAsDefault() throws Exception {
        doReturn(PACKAGE_LOCAL)
                .when(launcher)
                .defaultViewHandlerPackage(((PackageManager) any()), ((Uri) any()));
        doReturn(true)
                .when(launcher)
                .supportedCustomTabs(((PackageManager) any()), anyString());

        final String chrome = launcher.packageNameToUse(pm, uri);
        assertThat(chrome, equalTo(PACKAGE_LOCAL));
    }

    @Test
    public void packageNameToUse_notInstalledChrome() throws Exception {
        doReturn(null)
                .when(launcher)
                .defaultViewHandlerPackage(((PackageManager) any()), ((Uri) any()));
        doReturn(Collections.emptyList())
                .when(launcher)
                .installedPackages(((PackageManager) any()));

        final String packageName = launcher.packageNameToUse(pm, uri);
        assertThat(packageName, is(nullValue()));
    }

    @Test
    public void packageNameToUse_prioritizeStableChrome() throws Exception {
        doReturn(null)
                .when(launcher)
                .defaultViewHandlerPackage(((PackageManager) any()), ((Uri) any()));

        final List<String> candidates = asList(
                PACKAGE_LOCAL,
                PACKAGE_DEV,
                PACKAGE_BETA,
                PACKAGE_STABLE
        );
        doReturn(candidates)
                .when(launcher)
                .installedPackages(((PackageManager) any()));
        doReturn(true)
                .when(launcher)
                .supportedCustomTabs(((PackageManager) any()), anyString());

        final String stable = launcher.decidePackage(pm, candidates);
        assertThat(stable, equalTo(PACKAGE_STABLE));
    }
}