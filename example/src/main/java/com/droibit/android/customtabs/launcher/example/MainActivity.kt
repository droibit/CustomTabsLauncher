package com.droibit.android.customtabs.launcher.example

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.droibit.android.customtabs.launcher.CustomTabsLauncher
import com.droibit.android.customtabs.launcher.launch

class MainActivity : AppCompatActivity() {

    companion object {
        val GOOGLE = Uri.parse("https://www.google.com")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn_default)?.setOnClickListener {
            launchDefaultCustomTabs()
        }

        findViewById(R.id.btn_launcher)?.setOnClickListener {
            launchFromLauncher()
        }

        findViewById(R.id.btn_launcher_kotlin)?.setOnClickListener {
            launchFromKotlinLauncher()
        }

        // Try in an environment in which Chrome is not installed.
        findViewById(R.id.btn_fallbacks)?.setOnClickListener {
            fallbacks()
        }
    }

    private fun launchDefaultCustomTabs() {
        val tabsIntent = customTabsBuilder().build()
        tabsIntent.launchUrl(this, GOOGLE)
    }

    private fun launchFromLauncher() {
        val tabsIntent = customTabsBuilder().build()
        CustomTabsLauncher.launch(this, tabsIntent, GOOGLE)
    }

    private fun launchFromKotlinLauncher() {
        customTabsBuilder().build().launch(activity = this, uri = GOOGLE)
    }

    private fun fallbacks() {
        customTabsBuilder().build().launch(activity = this, uri = GOOGLE) { activity, uri ->
            Toast.makeText(activity, "Failed to launch Chrome Custom Tabs.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun customTabsBuilder(): CustomTabsIntent.Builder {
        return CustomTabsIntent.Builder()
                .setShowTitle(true)
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
