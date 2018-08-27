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

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Matchers.any;
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

  @Test public void launch_launchSuccess() {
    when(context.getPackageManager()).thenReturn(pm);

    final String packageName = "package.name.test";
    doReturn(packageName).when(launcher).getPackageNameToUse(any(), any(), any());

    final CustomTabsIntent customTabsIntent = spy(new CustomTabsIntent.Builder().build());
    final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
    //noinspection ConstantConditions
    launcher.launch(context, customTabsIntent, uri, singletonList("dummy"), fallback);

    verify(customTabsIntent).launchUrl(any(), same(uri));
    verify(fallback, never()).openUrl(any(), any(), same(customTabsIntent));
  }

  @Test public void launch_launchFailed() {
    when(mock(Context.class).getPackageManager()).thenReturn(pm).getMock();

    final CustomTabsFallback fallback = mock(CustomTabsFallback.class);
    doReturn(null).when(launcher).getPackageNameToUse(any(), any(), any());

    final CustomTabsIntent customTabsIntent = mock(CustomTabsIntent.class);
    //noinspection ConstantConditions
    launcher.launch(context, customTabsIntent, uri, singletonList("dummy"), fallback);
    verify(fallback).openUrl(any(), same(uri), same(customTabsIntent));
  }
}