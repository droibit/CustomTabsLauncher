package com.droibit.android.customtabs.launcher.example

import android.content.ActivityNotFoundException
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.droibit.android.customtabs.launcher.NonChromeCustomTabs
import com.droibit.android.customtabs.launcher.ensureChromeCustomTabsPackage

@Suppress("UNUSED_PARAMETER")
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val activityLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        Log.d("DEBUG", "result: $result")
    }

    fun launchDefaultCustomTabs(v: View) {
        try {
            val customTabsIntent = customTabsBuilder().build()
            customTabsIntent.launchUrl(this, URI_GOOGLE)
        } catch (e: ActivityNotFoundException) {
            showErrorToast()
        }
    }

    fun launchFromLauncher(v: View) {
        try {
            val customTabsIntent = customTabsBuilder()
                .build().also {
                    it.ensureChromeCustomTabsPackage(this)
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
                    it.ensureChromeCustomTabsPackage(this, fallback = NonChromeCustomTabs(this))
                }
            customTabsIntent.launchUrl(this, URI_GOOGLE)
        } catch (e: ActivityNotFoundException) {
            showErrorToast()
        }
    }

    fun startActivityWithCustomTabs(v: View) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(
                        ContextCompat.getColor(this, R.color.colorPrimary)
                    )
                    .build()
            )
            .setInitialActivityHeightPx(400)
            .build().apply {
                ensureChromeCustomTabsPackage(this@MainActivity)
                intent.data = URI_GOOGLE
            }
        activityLauncher.launch(customTabsIntent.intent)
    }

    private fun customTabsBuilder(): CustomTabsIntent.Builder {
        return CustomTabsIntent.Builder()
            .setUrlBarHidingEnabled(true)
            .setShowTitle(true)
            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(
                        ContextCompat.getColor(this, R.color.colorPrimary)
                    )
                    .build()
            )
            .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun showErrorToast() {
        Toast.makeText(this, "Failed to launch Chrome Custom Tabs.", Toast.LENGTH_SHORT)
            .show()
    }

    companion object {

        @JvmStatic
        private val URI_GOOGLE = Uri.parse("https://www.google.com")
    }
}
