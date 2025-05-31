package com.droibit.android.customtabs.launcher

import android.content.Context
import androidx.browser.auth.AuthTabIntent
import androidx.browser.customtabs.CustomTabsClient
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droibit.android.customtabs.launcher.CustomTabsPackage.CHROME_PACKAGES
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class AuthTabIntentHelperTest {
  @get:Rule
  val mockkRule = MockKRule(this)

  @MockK
  private lateinit var context: Context

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun `setChromeCustomTabsPackage uses Chrome if found`() {
    mockkStatic(CustomTabsClient::class)

    val chromePackage = "com.android.chrome"
    every { CustomTabsClient.getPackageName(any(), any(), any()) } returns chromePackage

    val authTabIntent = AuthTabIntent.Builder().build()
      .setChromeCustomTabsPackage(context)
    assertThat(authTabIntent.intent.`package`).isEqualTo(chromePackage)

    verify {
      val packages = CHROME_PACKAGES.toList()
      CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
    }
  }

  @Test
  fun `setChromeCustomTabsPackage sets null if Chrome not found`() {
    mockkStatic(CustomTabsClient::class)
    every { CustomTabsClient.getPackageName(any(), any(), any()) } returns null

    val authTabIntent = AuthTabIntent.Builder().build()
      .setChromeCustomTabsPackage(context)
    assertThat(authTabIntent.intent.`package`).isNull()

    verify {
      val packages = CHROME_PACKAGES.toList()
      CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
    }
  }

  @Test
  fun `setChromeCustomTabsPackage uses non-Chrome Custom Tab if Chrome not found`() {
    mockkStatic(CustomTabsClient::class)

    val nonChromePackage = "com.example.customtabs"
    every { CustomTabsClient.getPackageName(any(), any(), any()) } returns nonChromePackage

    val additionalAuthTabs = NonChromeCustomTabs(setOf(nonChromePackage))
    val authTabIntent = AuthTabIntent.Builder().build()
      .setChromeCustomTabsPackage(context, additionalAuthTabs)
    assertThat(authTabIntent.intent.`package`).isEqualTo(nonChromePackage)

    verify {
      val packages = (CHROME_PACKAGES + nonChromePackage).toList()
      CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
    }
  }

  @Test
  fun `setChromeCustomTabsPackage does not set package if getCustomTabsPackage returns null`() {
    mockkStatic(CustomTabsClient::class)
    every { CustomTabsClient.getPackageName(any(), any(), any()) } returns null

    val authTabIntent = AuthTabIntent.Builder().build()
      .also {
        assertThat(it.intent.`package`).isNull()
      }
      .setChromeCustomTabsPackage(context)

    assertThat(authTabIntent.intent.`package`).isNull()

    verify {
      val packages = CHROME_PACKAGES.toList()
      CustomTabsClient.getPackageName(any(), eq(packages), eq(true))
    }
  }
}
