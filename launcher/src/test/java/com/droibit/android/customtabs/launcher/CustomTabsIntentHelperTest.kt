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
import org.mockito.Mockito.mockStatic
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.times
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class CustomTabsIntentHelperTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var context: Context

    @Test
    fun `ensureChromeCustomTabsPackage uses Chrome if found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val chromePackage = "com.android.chrome"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(chromePackage)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .ensureChromeCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(chromePackage)

            mocked.verify {
                CustomTabsClient.getPackageName(any(), any(), eq(true))
            }
        }
    }

    @Test
    fun `ensureChromeCustomTabsPackage sets null if Chrome not found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(null)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .ensureChromeCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isNull()

            mocked.verify {
                CustomTabsClient.getPackageName(any(), any(), eq(true))
            }
        }
    }

    @Test
    fun `ensureChromeCustomTabsPackage falls back to non-Chrome Custom Tab if Chrome not found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val noneChromePackage = "com.example.customtabs"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(null, noneChromePackage)

            val fallback = NonChromeCustomTabs(listOf(noneChromePackage))
            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .ensureChromeCustomTabsPackage(context, fallback)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(noneChromePackage)

            inOrder(CustomTabsClient::class.java) {
                verify(mocked) {
                    CustomTabsClient.getPackageName(any(), any(), eq(true))
                }
                verify(mocked) {
                    CustomTabsClient.getPackageName(any(), any(), eq(false))
                }
            }
        }
    }

    @Test
    fun `ensureCustomTabsPackage uses default browser if found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val chromePackage = "com.chrome.beta"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(chromePackage)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .ensureCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(chromePackage)

            mocked.verify {
                CustomTabsClient.getPackageName(any(), any(), eq(false))
            }
        }
    }

    @Test
    fun `ensureCustomTabsPackage sets null if no default browser or Chrome found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(null)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .ensureCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isNull()

            mocked.verify {
                CustomTabsClient.getPackageName(any(), any(), eq(false))
            }
        }
    }

    @Test
    fun `ensureCustomTabsPackage falls back to non-Chrome Custom Tab if none found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val noneChromePackage = "com.example.customtabs"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(null, noneChromePackage)

            val fallback = NonChromeCustomTabs(listOf(noneChromePackage))
            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .ensureCustomTabsPackage(context, fallback)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(noneChromePackage)

            mocked.verify({
                CustomTabsClient.getPackageName(any(), any(), eq(false))
            }, times(2))
        }
    }
}