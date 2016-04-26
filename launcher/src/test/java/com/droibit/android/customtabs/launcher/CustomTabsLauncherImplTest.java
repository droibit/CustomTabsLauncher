package com.droibit.android.customtabs.launcher;

import android.content.pm.PackageManager;

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

/**
 * @author kumagai
 */
public class CustomTabsLauncherImplTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Spy
    CustomTabsLauncherImpl launcher;

    @Test
    public void getStableChrome() {
        doReturn(true).when(launcher)
                .supportedCustomTabs(any(PackageManager.class), anyString());

        final List<String> candidates = singletonList(PACKAGE_STABLE);
        final String stable = launcher.packageNameToUse(candidates, null);
        assertThat(stable, equalTo(PACKAGE_STABLE));
    }

    @Test
    public void getStableChromeFromMultipleVersions() {
        doReturn(true).when(launcher)
                .supportedCustomTabs(any(PackageManager.class), anyString());

        final List<String> candidates = asList(
                PACKAGE_LOCAL,
                PACKAGE_DEV,
                PACKAGE_BETA,
                PACKAGE_STABLE);
        final String stable = launcher.packageNameToUse(candidates, null);
        assertThat(stable, equalTo(PACKAGE_STABLE));
    }

    @Test
    public void getBetaChrome() {
        doReturn(false).when(launcher)
                .supportedCustomTabs(any(PackageManager.class), eq(PACKAGE_STABLE));
        doReturn(true).when(launcher)
                .supportedCustomTabs(any(PackageManager.class), eq(PACKAGE_BETA));

        final List<String> candidates = asList(
                PACKAGE_BETA,
                PACKAGE_STABLE);
        final String beta = launcher.packageNameToUse(candidates, null);
        assertThat(beta, equalTo(PACKAGE_BETA));
    }
}