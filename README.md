# CustomTabsLauncher

[![Android CI](https://github.com/droibit/CustomTabsLauncher/actions/workflows/android.yml/badge.svg)](https://github.com/droibit/CustomTabsLauncher/actions/workflows/android.yml)
[![Download](https://img.shields.io/maven-central/v/io.github.droibit/customtabslauncher/3.0.0)](https://central.sonatype.com/artifact/io.github.droibit/customtabslauncher/3.0.0)
[![Software License](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](https://github.com/droibit/prefbinding/blob/develop/LICENSE)

This library makes it easy to launch [Custom Tabs](https://developer.chrome.com/docs/android/custom-tabs) directly from your app.

In some user environments, Custom Tabs may not launch directly. This can happen in the following cases:

- Multiple browser apps are installed on the device.
- The default browser is not Chrome.

Custom Tabs can be displayed as one screen of your app, allowing you to customize the look and feel.  
For this reason, I created this library to make it easier to launch Chrome Custom Tabs directly.

## Download

Add the following dependency to your `build.gradle` file:

```kotlin
implementation("io.github.droibit:customtabslauncher:LATEST_VERSION")

// Recommended: Declare androidx.browser explicitly as a dependency.
// implementation 'androidx.browser:browser:LATEST_VERSION'
```

## Usage

### Basic Usage

#### Launch in Chrome Custom Tabs

```kotlin
try {
    val customTabsIntent = buildCustomTabsIntent()
        .setChromeCustomTabsPackage(context)
    customTabsIntent.launchUrl(context, Uri.parse("https://example.com"))
} catch (e: ActivityNotFoundException) {
    // Launch WebView, display a toast, etc.     
}
```

#### Launch in the default browser that supports Custom Tabs

```kotlin
try {
    val customTabsIntent = buildCustomTabsIntent()
        .setCustomTabsPackage(context)
    customTabsIntent.launchUrl(context, Uri.parse("https://example.com"))
} catch (e: ActivityNotFoundException) {
    // Launch WebView, display a toast, etc.     
}
```

### Present a custom tab as bottom sheet

```kotlin
val activityLauncher = registerForActivityResult(StartActivityForResult()) {
     // Do something.
}

try {
  val customTabsIntent = build().apply {
      setChromeCustomTabsPackage(context) // or setCustomTabsPackage(context)
      intent.data = Uri.parse("https://example.com")
  }
  activityLauncher.launch(customTabsIntent.intent)
} catch (e: ActivityNotFoundException) {
    // Launch WebView, display a toast, etc.     
}
```

### Fallback to a non-Chrome browser

```kotlin
buildCustomTabsIntent()
  .setChromeCustomTabsPackage( // or .setCustomTabsPackage(
      context,
      // Launch a browser that supports Custom Tabs (non-Chrome).
      NonChromeCustomTabs(context)
      // or launch a specific browser that supports Custom Tabs.
      // NonChromeCustomTabs(
      //   listOf("org.mozilla.firefox", "com.microsoft.emmx")
      // )
    )
```

## License

    Copyright (C) 2015 The Android Open Source Project
    Copyright (C) 2016 Shinya Kumagai

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
