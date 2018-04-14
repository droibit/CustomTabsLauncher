package com.droibit.android.customtabs.launcher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Mock private PackageManager pm;

  @Mock private Uri uri;

  @Mock private Context context;

  @Spy private CustomTabsLauncherImpl launcher;

  @Test public void canLaunch() {
    doReturn(PACKAGE_STABLE).when(launcher).packageNameToUse(any(), any());
    assertThat(launcher.canLaunch(context, uri), is(true));

    doReturn(null).when(launcher).packageNameToUse(any(), any());
    assertThat(launcher.canLaunch(context, uri), is(false));
  }

  @Test public void launch_launchSuccess() throws Exception {
    when(context.getPackageManager()).thenReturn(pm).getMock();

    final String packageName = "package.name.test";
    doReturn(packageName).when(launcher).packageNameToUse(any(), any());

    final CustomTabsIntent customTabsIntent = spy(new CustomTabsIntent.Builder().build());
    final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
    //noinspection ConstantConditions
    launcher.launch(context, customTabsIntent, uri, fallback);

    verify(customTabsIntent).launchUrl(any(), same(uri));
    verify(fallback, never()).openUrl(any(), any());
  }

  @Test public void launch_launchFailed() throws Exception {
    when(mock(Context.class).getPackageManager()).thenReturn(pm).getMock();

    final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
    doReturn(null).when(launcher).packageNameToUse(any(), any());

    //noinspection ConstantConditions
    launcher.launch(context, null, uri, fallback);
    verify(fallback).openUrl(any(), same(uri));
  }

  @Test public void packageNameToUse_useChromeAsDefault() throws Exception {
    doReturn(PACKAGE_LOCAL).when(launcher).defaultViewHandlerPackage(any(), any());
    doReturn(true).when(launcher).supportedCustomTabs(any(), anyString());

    final String chrome = launcher.packageNameToUse(pm, uri);
    assertThat(chrome, equalTo(PACKAGE_LOCAL));
  }

  @Test public void packageNameToUse_notInstalledChrome() throws Exception {
    doReturn(null).when(launcher).defaultViewHandlerPackage(any(), any());
    doReturn(Collections.emptyList()).when(launcher).installedPackages(any());

    final String packageName = launcher.packageNameToUse(pm, uri);
    assertThat(packageName, is(nullValue()));
  }

  @Test public void packageNameToUse_prioritizeStableChrome() throws Exception {
    doReturn(null).when(launcher).defaultViewHandlerPackage(any(), any());

    final List<String> candidates =
        asList(PACKAGE_LOCAL, PACKAGE_DEV, PACKAGE_BETA, PACKAGE_STABLE);
    doReturn(candidates).when(launcher).installedPackages(any());
    doReturn(true).when(launcher).supportedCustomTabs(any(), anyString());

    final String stable = launcher.decidePackage(pm, candidates);
    assertThat(stable, equalTo(PACKAGE_STABLE));
  }
}