package com.droibit.android.customtabs.launcher.example

import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.droibit.android.customtabs.launcher.CustomTabsLauncher
import com.droibit.android.customtabs.launcher.launch

@Suppress("UNUSED_PARAMETER")
class MainActivity : AppCompatActivity() {

  companion object {

    @JvmStatic
    private val GOOGLE = Uri.parse("https://www.google.com")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun launchDefaultCustomTabs(v: View) {
    try {
      val tabsIntent = customTabsBuilder().build()
      tabsIntent.launchUrl(this, GOOGLE)
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  fun launchFromLauncher(v: View) {
    try {
      val tabsIntent = customTabsBuilder().build()
      CustomTabsLauncher.launch(this, tabsIntent, GOOGLE)
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  fun launchFromKotlinLauncher(v: View) {
    try {
      customTabsBuilder().build()
          .launch(this, url = GOOGLE)
    } catch (e: ActivityNotFoundException) {
      showErrorToast()
    }
  }

  fun fallbacks(v: View) {
    customTabsBuilder().build()
        .launch(this, url = GOOGLE) { _, _ ->
          showErrorToast()
        }
  }

  fun canLaunch(v: View) {
    val canLaunch = CustomTabsLauncher.canLaunch(this, GOOGLE)
    Toast.makeText(this, "can launch=$canLaunch, url=$GOOGLE", Toast.LENGTH_SHORT).show()
  }

  private fun customTabsBuilder(): CustomTabsIntent.Builder {
    return CustomTabsIntent.Builder()
        .enableUrlBarHiding()
        .setShowTitle(true)
        .addDefaultShareMenuItem()
        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
        .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
  }

  private fun showErrorToast() {
    Toast.makeText(this, "Failed to launch Chrome Custom Tabs.", Toast.LENGTH_SHORT)
        .show()
  }
}
