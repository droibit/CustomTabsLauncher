package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droibit.android.customtabs.launcher.CustomTabsPackage.CHROME_PACKAGES
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
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class CustomTabsIntentHelperTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var context: Context

    @Test
    fun `CHROME_PACKAGES are listed in priority order`() {
        val expectedOrder = listOf(
            "com.android.chrome",
            "com.chrome.beta",
            "com.chrome.dev",
            "com.google.android.apps.chrome"
        )
        assertThat(CHROME_PACKAGES.toList()).isEqualTo(expectedOrder)
    }

    @Test
    fun `setChromeCustomTabsPackage uses Chrome if found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val chromePackage = "com.android.chrome"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(chromePackage)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .setChromeCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(chromePackage)

            mocked.verify {
                val packages = CHROME_PACKAGES.toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
            }
        }
    }

    @Test
    fun `setChromeCustomTabsPackage sets null if Chrome not found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(null)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .setChromeCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isNull()

            mocked.verify {
                val packages = CHROME_PACKAGES.toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
            }
        }
    }

    @Test
    fun `setChromeCustomTabsPackage uses non-Chrome Custom Tab if Chrome not found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val nonChromePackage = "com.example.customtabs"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(nonChromePackage)

            val additionalCustomTabs = NonChromeCustomTabs(setOf(nonChromePackage))
            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .setChromeCustomTabsPackage(context, additionalCustomTabs)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(nonChromePackage)

            inOrder(CustomTabsClient::class.java) {
                verify(mocked) {
                    val packages = (CHROME_PACKAGES + nonChromePackage).toList()
                    CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
                }
            }
        }
    }

    @Test
    fun `setCustomTabsPackage uses default browser if found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val chromePackage = "com.chrome.beta"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(chromePackage)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .setCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(chromePackage)

            mocked.verify {
                val packages = CHROME_PACKAGES.toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(false))
            }
        }
    }

    @Test
    fun `setCustomTabsPackage sets null if no default browser or Chrome found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(null)

            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .setCustomTabsPackage(context)
            assertThat(customTabsIntent.intent.`package`).isNull()

            mocked.verify {
                val packages = CHROME_PACKAGES.toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(false))
            }
        }
    }

    @Test
    fun `setCustomTabsPackage uses non-Chrome Custom Tab if none found`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val nonChromePackage = "com.example.customtabs"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(nonChromePackage)

            val additionalCustomTabs = NonChromeCustomTabs(setOf(nonChromePackage))
            val customTabsIntent = CustomTabsIntent.Builder()
                .build()
                .setCustomTabsPackage(context, additionalCustomTabs)
            assertThat(customTabsIntent.intent.`package`).isEqualTo(nonChromePackage)

            mocked.verify {
                val packages = (CHROME_PACKAGES + nonChromePackage).toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(false))
            }
        }
    }

    @Test
    fun `getCustomTabsPackage returns default Chrome package when available`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val chromePackage = "com.android.chrome"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(chromePackage)

            val packageName = getCustomTabsPackage(context)
            assertThat(packageName).isEqualTo(chromePackage)

            mocked.verify {
                val packages = CHROME_PACKAGES.toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
            }
        }
    }

    @Test
    fun `getCustomTabsPackage returns additional Custom Tabs package when Chrome not available`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val nonChromePackage = "com.example.browser"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(nonChromePackage)

            val additionalCustomTabs = NonChromeCustomTabs(setOf(nonChromePackage))
            val packageName =
                getCustomTabsPackage(context, additionalCustomTabs = additionalCustomTabs)
            assertThat(packageName).isEqualTo(nonChromePackage)

            mocked.verify {
                val packages = (CHROME_PACKAGES + nonChromePackage).toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
            }
        }
    }

    @Test
    fun `getCustomTabsPackage returns null when no packages are available`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(null)

            val packageName = getCustomTabsPackage(context)
            assertThat(packageName).isNull()

            mocked.verify {
                val packages = CHROME_PACKAGES.toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
            }
        }
    }

    @Test
    fun `getCustomTabsPackage ignores default packages when ignoreDefault is false`() {
        mockStatic(CustomTabsClient::class.java).use { mocked ->
            val nonChromePackage = "com.example.browser"
            mocked.`when`<String?> {
                CustomTabsClient.getPackageName(any(), any(), any())
            }.thenReturn(nonChromePackage)

            val packageName = getCustomTabsPackage(
                context,
                ignoreDefault = false
            )
            assertThat(packageName).isEqualTo(nonChromePackage)

            mocked.verify {
                val packages = CHROME_PACKAGES.toList()
                CustomTabsClient.getPackageName(any(), eq(packages), eq(false))
            }
        }
    }
}