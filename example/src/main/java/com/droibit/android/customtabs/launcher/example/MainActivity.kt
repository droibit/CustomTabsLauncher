package com.droibit.android.customtabs.launcher.example

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.auth.AuthTabIntent
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.droibit.android.customtabs.launcher.NonChromeCustomTabs
import com.droibit.android.customtabs.launcher.setChromeCustomTabsPackage
import com.droibit.android.customtabs.launcher.setCustomTabsPackage

@Suppress("UNUSED_PARAMETER")
class MainActivity : AppCompatActivity(R.layout.activity_main) {
  private val partialCustomTabsLauncher =
    registerForActivityResult(StartActivityForResult()) { result ->
      Log.d(BuildConfig.BUILD_TYPE, "result: $result")
    }

  private val authTabLauncher = AuthTabIntent.registerActivityResultLauncher(this) { result ->
    Log.d(BuildConfig.BUILD_TYPE, "result: ${result.resultCode}, resultUri=${result.resultUri}")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.light(
        scrim = Color.TRANSPARENT,
        darkScrim = Color.TRANSPARENT,
      ),
    )
    super.onCreate(savedInstanceState)
    setSupportActionBar(findViewById(R.id.toolbar))

    // Apply system bar insets to the root view to support edge-to-edge layout.
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
      val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.updatePadding(top = systemInsets.top, bottom = systemInsets.bottom)
      WindowInsetsCompat.CONSUMED
    }
    dumpInstalledBrowsers(this)
  }

  fun launchInDefaultCustomTabs(v: View) {
    try {
      val customTabsIntent = customTabsBuilder().build()
      customTabsIntent.launchUrl(this, URI_GOOGLE)
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  fun launchInChromeCustomTabs(v: View) {
    try {
      val customTabsIntent = customTabsBuilder()
        .build().also {
          it.setChromeCustomTabsPackage(this)
        }
      customTabsIntent.launchUrl(this, URI_GOOGLE)
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  fun launchInCustomTabs(v: View) {
    try {
      val customTabsIntent = customTabsBuilder()
        .build().also {
          it.setCustomTabsPackage(this)
        }
      customTabsIntent.launchUrl(this, URI_GOOGLE)
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  fun fallbacks(v: View) {
    try {
      val customTabsIntent = customTabsBuilder()
        .build().also {
          it.setChromeCustomTabsPackage(this, NonChromeCustomTabs(this))
        }
      customTabsIntent.launchUrl(this, URI_GOOGLE)
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  fun launchInPartialCustomTabs(v: View) {
    val customTabsIntent = CustomTabsIntent.Builder()
      .setShowTitle(true)
      .setDefaultColorSchemeParams(
        CustomTabColorSchemeParams.Builder()
          .setToolbarColor(
            ContextCompat.getColor(this, R.color.colorPrimary),
          )
          .build(),
      )
      .setInitialActivityHeightPx(400)
      .build().apply {
        setChromeCustomTabsPackage(this@MainActivity)
        intent.data = URI_GOOGLE
      }
    partialCustomTabsLauncher.launch(customTabsIntent.intent)
  }

  fun launchAuthTab(v: View) {
    try {
      val authTabIntent = AuthTabIntent.Builder()
        .build()
        .setChromeCustomTabsPackage(this)
      authTabIntent.launch(authTabLauncher, URI_GOOGLE, "example")
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  private fun customTabsBuilder(): CustomTabsIntent.Builder {
    return CustomTabsIntent.Builder()
      .setUrlBarHidingEnabled(true)
      .setShowTitle(true)
      .setShareState(CustomTabsIntent.SHARE_STATE_ON)
      .setDefaultColorSchemeParams(
        CustomTabColorSchemeParams.Builder()
          .setToolbarColor(
            ContextCompat.getColor(this, R.color.colorPrimary),
          )
          .build(),
      )
  }

  private fun showErrorToast() {
    Toast.makeText(this, "Failed to launch Chrome Custom Tabs.", Toast.LENGTH_SHORT)
      .show()
  }

  private fun dumpInstalledBrowsers(context: Context) {
    val pm = context.packageManager
    val intent = Intent(ACTION_VIEW, "http://www.example.com".toUri())
    val flag = PackageManager.MATCH_ALL
    val browser = buildList {
      val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        pm.queryIntentActivities(
          intent,
          PackageManager.ResolveInfoFlags.of(flag.toLong()),
        )
      } else {
        pm.queryIntentActivities(intent, flag)
      }
      for (info in resolveInfos) {
        val packageName = info.activityInfo.packageName
        val isAuthTabSupported = CustomTabsClient.isAuthTabSupported(context, packageName)
        add("$packageName(isAuthTabSupported=$isAuthTabSupported)")
      }
    }
    Log.d("DEBUG", "Installed browsers: $browser")
  }

  companion object {

    @JvmStatic
    private val URI_GOOGLE = "https://www.google.com".toUri()
  }
}
