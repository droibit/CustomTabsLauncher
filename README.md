# CustomTabsLauncher
[![Build Status](https://travis-ci.org/droibit/CustomTabsLauncher.svg?branch=develop)](https://travis-ci.org/droibit/CustomTabsLauncher) [![JitPack.io](https://jitpack.io/v/droibit/customtabslauncher.svg)](https://jitpack.io/#droibit/customtabslauncher) [![Software License](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg)](https://github.com/droibit/prefbinding/blob/develop/LICENSE)

This library to launch the [Chrome Custom Tabs](https://developer.chrome.com/multidevice/android/customtabs) directly.
Custom Tabs does not launch directly in the following user environment.

* Multiple browser app is installed.
* Default browser other than Chrome.

Custom Tabs can be displayed as one screen of the app to customize the look & feel. For this reason, I have created a library for launching direct.

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
implementation 'com.github.droibit.customtabslauncher:launcher:LATEST_VERSION'
// for kotlin
implementation 'com.github.droibit.customtabslauncher:launcher-kotlin:LATEST_VERSION'
```

## Usage

Java:
```java
CustomTabsIntent tabsIntent = createCustomTabsIntent();
CustomTabsLauncher.launch(
    activity,
    tabsIntent,
    Uri.parse("https://www.google.com"),
    new CustomTabsFallback() {
        @Override public void openUri(
          @NonNull Context context,
          @NonNull Uri url,
          @NonNull CustomTabsintent customTabsintent) {
            // Fallback is optional.
            // Launch WebView, display a toast, etc.
        }
    }
);
```

Kotlin:

```kotlin
createCustomTabsIntent().launch(context, url = "https://www.google.com") { context, url, customTabsintent ->
    // Fallback is optional.
    // Launch WebView, display a toast, etc.
}
```

#### Priority of Chrome

1. [Chrome](https://play.google.com/store/apps/details?id=com.android.chrome)
2. [Chrome Beta](https://play.google.com/store/apps/details?id=com.chrome.beta)
3. [Chrome Dev](https://play.google.com/store/apps/details?id=com.chrome.dev)
4. Local(com.google.android.apps.chrome)

#### Launch non-Chrome browsers as Fallback

This library officially supports Chrome,   
but provides `LaunchNonChromeCustomTabsFallback` as a helper class for directly launching other browsers that support CustomTabs.

```Java
static import com.droibit.android.customtabs.launcher.CustomTabsLauncher.LaunchNonChromeCustomTabs;

final exampleNonChromePackages = Arrays.asList(
  "org.mozilla.firefox",
  "com.microsoft.emmx"
);

CustomTabsLauncher.launch(
    activity,
    tabsIntent,
    Uri.parse("https://www.google.com"),
    new LaunchNonChromeCustomTabs(exampleNonChromePackages)
);
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
