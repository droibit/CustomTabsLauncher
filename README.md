# CustomTabsLauncher
[![Android CI](https://github.com/droibit/CustomTabsLauncher/workflows/Android%20CI/badge.svg)](https://github.com/droibit/CustomTabsLauncher/actions?query=workflow%3A%22Android+CI%22) [![JitPack.io](https://jitpack.io/v/droibit/customtabslauncher.svg)](https://jitpack.io/#droibit/customtabslauncher) [![Software License](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](https://github.com/droibit/prefbinding/blob/develop/LICENSE)

This library to launch the [Custom Tabs](https://developer.chrome.com/docs/android/custom-tabs/) directly.
Custom Tabs does not launch directly in the following user environment.
* Multiple browser app is installed.
* Default browser is non-Chrome.

Custom Tabs can be displayed as one screen of the app to customize the look & feel. For this reason, I have created a library for launching direct.

### Browser Priorities

1. [Chrome](https://play.google.com/store/apps/details?id=com.android.chrome).
2. [Chrome Beta](https://play.google.com/store/apps/details?id=com.chrome.beta).
3. [Chrome Dev](https://play.google.com/store/apps/details?id=com.chrome.dev).
4. Local(com.google.android.apps.chrome).
5. (Optional) Browsers provided by `CustomTabsPackageFallback`.

## Download

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
  repositories {
    maven { url "https://jitpack.io" }
  }
}
```

Add the dependency

```groovy
implementation 'com.github.droibit:customtabslauncher:LATEST_VERSION'
```

## Usage

#### Basic usage

```kotlin
try {
    val customTabsIntent = buildCustomTabsIntent()
        .ensureCustomTabsPackage(context)
    customTabsIntent.launchUrl(context, Uri.parse("https://example.com"))
} catch (e: ActivityNotFoundException) {
    // Launch WebView, display a toast, etc.     
}
```

##### Present the custom tab as bottom sheet

```kotlin
val activityLauncher = registerForActivityResult(StartActivityForResult()) {
     // Do something.
}

try {
  val customTabsIntent = build().apply {
      ensureCustomTabsPackage(context)
      intent.data = Uri.parse("https://example.com")
  }
  activityLauncher.launch(customTabsIntent.intent)
} catch (e: ActivityNotFoundException) {
    // Launch WebView, display a toast, etc.     
}
```

#### How to use `CustomTabsPackageFallback`

```kotlin
buildCustomTabsIntent()
  .ensureCustomTabsPackage(
      context,
      // Launch a specific browser that supports Custom Tabs.
      NonChromeCustomTabs(
        listOf(
          "org.mozilla.firefox",
          "com.microsoft.emmx"
        )
        // or launch a browser that supports Custom Tabs (not Chrome).
        // NonChromeCustomTabs(context)
      )
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
