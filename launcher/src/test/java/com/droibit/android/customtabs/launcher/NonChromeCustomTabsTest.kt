package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class NonChromeCustomTabsTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var context: Context

    @Test
    fun `setCustomTabsPackage sets package to first available non-Chrome Custom Tab`() {
        Mockito.mockStatic(CustomTabsClient::class.java).use { mocked ->
            val noneChromePackage1 = "com.example.customtabs_1"
            val noneChromePackage2 = "com.example.customtabs_2"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(noneChromePackage1)

            val fallback = NonChromeCustomTabs(listOf(noneChromePackage1, noneChromePackage2))
            val customTabsIntent = CustomTabsIntent.Builder().build()
            with(fallback) {
                customTabsIntent.setCustomTabsPackage(context)
            }
            assertThat(customTabsIntent.intent.`package`).isEqualTo(noneChromePackage1)

            mocked.verify {
                CustomTabsClient.getPackageName(any(), any(), eq(false))
            }
        }
    }

    @Test
    fun `setCustomTabsPackage does not set package if no non-Chrome Custom Tab found`() {
        Mockito.mockStatic(CustomTabsClient::class.java).use { mocked ->
            val fallback = NonChromeCustomTabs(emptyList())
            val customTabsIntent = CustomTabsIntent.Builder().build()
            with(fallback) {
                customTabsIntent.setCustomTabsPackage(context)
            }
            assertThat(customTabsIntent.intent.`package`).isNull()

            mocked.verify({
                CustomTabsClient.getPackageName(any(), any(), any())
            }, never())
        }
    }
}