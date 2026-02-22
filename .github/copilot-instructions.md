# Project Guidelines

## Overview

CustomTabsLauncher is a lightweight Android library that reliably launches
[Custom Tabs](https://developer.chrome.com/docs/android/custom-tabs) and
Auth Tab by explicitly resolving the correct browser package before launching.
It targets API 19+.

Modules:

- `:launcher` — the published library module
- `:example` — sample app, not part of the release artifact

## API Design & Conventions

- **API Surface**: Prefer **extension functions** on `CustomTabsIntent` / `AuthTabIntent`. Do not create wrapper or builder classes.
- **Visibility**: Keep implementation details `internal`. Only expose extension functions and `fun interface` types as public API.
- **Java Interoperability**: Annotate public functions with `@JvmName` / `@JvmOverloads` when the name or signature would be ambiguous or unidiomatic for Java callers.
- **Documentation**: Add KDoc to every public symbol, including a brief code example where helpful.
- **Version Catalog**: Use `gradle/libs.versions.toml` for all dependency and version management. Do not hardcode versions in `build.gradle.kts`.

## Testing

Test stack: JUnit 4 + [Robolectric](https://robolectric.org/) (`AndroidJUnit4`) + [MockK](https://mockk.io/) + [Google Truth](https://truth.dev/).
- Use Truth's `assertThat(…).isEqualTo(…)` style assertions.
- Use `MockKRule` and `mockkStatic` for static/object mocking.
  - Tear down with `unmockkAll()` in `@After`.
- Tests run as JVM unit tests — no emulator needed.

## Build and Test Commands

Project-wide:

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

Library module only:

```bash
./gradlew :launcher:assembleDebug
./gradlew :launcher:testDebugUnitTest
```
