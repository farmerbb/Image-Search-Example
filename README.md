# Android Image Search Example App

Every new project that I start is an opportunity to do better – to implement best practices that are up-to-date and consistent with Google's recommendations.  This image search example app is an attempt to implement these best practices as of January 2021:

* Google's [recommended app architecture](https://developer.android.com/jetpack/guide)*: single activity w/fragments, ViewModel underneath, repository underneath that
* Dependency injection (using [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android))
* [Jetpack libraries](https://developer.android.com/jetpack), including: paging, navigation, lifecycle, ViewBinding, among others
* REST API interaction using [Retrofit](https://square.github.io/retrofit/), paired with [Moshi](https://github.com/square/moshi) for JSON parsing
* 100% [Kotlin](https://kotlinlang.org/) 😎

<sup>* Data persistence is not included in this example app.  For an example on how to persist data using [Room](https://developer.android.com/training/data-storage/room), check out [AppNotifier](https://github.com/farmerbb/AppNotifier).</sup>

## Features

* Search for images using [Imgur's API](https://api.imgur.com/)
* View images fullscreen with pinch-to-zoom

## Configuration

The app can be used out-of-the-box with some minimal configuration.

### Imgur Client ID
This is required for the Imgur API to work.  You can obtain this by registering your application with Imgur [here](https://api.imgur.com/oauth2/addclient).

* Location: [ImageSearchModule.kt line 56](https://github.com/farmerbb/Image-Search-Example/blob/master/app/src/main/java/com/example/imagesearch/ImageSearchModule.kt#L56)

## How to Build
Prerequisites:
* Windows / MacOS / Linux
* JDK 8
* Android SDK
* Internet connection (to download dependencies)

First, ensure that you have added an Imgur Client ID to the app, as described in the Configuration section above.  Once all the prerequisites are met, make sure that the `ANDROID_HOME` environment variable is set to your Android SDK directory, then run `./gradlew assembleDebug` at the base directory of the project to start the build. After the build completes, navigate to `app/build/outputs/apk/debug` where you will end up with an APK file ready to install on your Android device.