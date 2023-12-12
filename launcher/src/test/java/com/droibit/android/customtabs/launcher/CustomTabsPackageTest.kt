package com.droibit.android.customtabs.launcher

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class CustomTabsPackageTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var pm: PackageManager

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        whenever(context.packageManager).doReturn(pm)
    }

    @Test
    fun `getNonChromeCustomTabsPackages returns only non-Chrome CustomTabs packages`() {
        whenever(pm.queryIntentActivities(any(), anyInt())).doReturn(
            listOf(
                createResolveInfo(packageName = "com.android.chrome"),
                createResolveInfo(packageName = "com.example.customtabs_1"),
                createResolveInfo(packageName = "com.example.customtabs_2"),
            )
        )
        whenever(pm.resolveService(any(), anyInt())).doReturn(mock())

        val result = CustomTabsPackage.getNonChromeCustomTabsPackages(context)
        assertThat(result).containsExactly(
            "com.example.customtabs_1",
            "com.example.customtabs_2",
        )
    }

    @Test
    fun `getNonChromeCustomTabsPackages returns empty list if CustomTabsService is not found`() {
        whenever(pm.queryIntentActivities(any(), anyInt())).doReturn(
            listOf(
                createResolveInfo(packageName = "com.example.customtabs_1"),
                createResolveInfo(packageName = "com.example.customtabs_2"),
            )
        )
        whenever(pm.resolveService(any(), anyInt())).doReturn(null)

        val result = CustomTabsPackage.getNonChromeCustomTabsPackages(context)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getNonChromeCustomTabsPackages returns empty list if Chrome is installed`() {
        whenever(pm.queryIntentActivities(any(), anyInt())).doReturn(
            listOf(
                createResolveInfo(packageName = "com.android.chrome"),
                createResolveInfo(packageName = "com.chrome.beta"),
            )
        )
        whenever(pm.resolveService(any(), anyInt())).doReturn(mock())

        val result = CustomTabsPackage.getNonChromeCustomTabsPackages(context)
        assertThat(result).isEmpty()
    }
}

private fun createResolveInfo(packageName: String) = ResolveInfo().apply {
    activityInfo = ActivityInfo().apply {
        this.packageName = packageName
    }
}