package com.droibit.android.customtabs.launcher.example

import android.content.ActivityNotFoundException
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.droibit.android.customtabs.launcher.CustomTabsLauncher
import com.droibit.android.customtabs.launcher.CustomTabsLauncher.LaunchNonChromeCustomTabs
import com.droibit.android.customtabs.launcher.launch

@Suppress("UNUSED_PARAMETER")
class MainActivity : AppCompatActivity(R.layout.activity_main) {

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
            val customTabsIntent = customTabsBuilder().build()
            CustomTabsLauncher.launch(this, customTabsIntent, URI_GOOGLE)
        } catch (e: ActivityNotFoundException) {
            showErrorToast()
        }
    }

    fun launchFromKotlinLauncher(v: View) {
        try {
            customTabsBuilder().build()
                .launch(this, URI_GOOGLE)
        } catch (e: ActivityNotFoundException) {
            showErrorToast()
        }
    }

    fun fallbacks(v: View) {
        val customTabsIntent = customTabsBuilder().build()
        CustomTabsLauncher.launch(
            this,
            customTabsIntent,
            URI_GOOGLE,
            LaunchNonChromeCustomTabs(this)
        )
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
