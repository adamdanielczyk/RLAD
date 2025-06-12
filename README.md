# RLAD - Random List And Details

RLAD is a playground for trying out mobile development patterns in both native Android and React Native. This repository contains two separate apps:

- **native-android/** – the original Android application written in Kotlin using Jetpack Compose and other modern libraries.
- **expo/** – a cross‑platform app built with React Native via Expo.

Both apps use public APIs to display lists of items and their details. The Android version is available on [Google Play](https://play.google.com/store/apps/details?id=com.rlad).

## APIs

- [Art Institute of Chicago](https://api.artic.edu)
- [Giphy](https://developers.giphy.com/docs/api/endpoint)
- [Rick And Morty](https://rickandmortyapi.com/documentation)

## Running the native Android app

1. Clone the repository.
2. Generate a Giphy API key on [developers.giphy.com](https://developers.giphy.com).
3. Add the key to `~/.gradle/gradle.properties` as `GIPHY_API_KEY=YOUR_KEY`.
4. Open the `native-android` project in Android Studio and build/run it.

The Android app is modularized into feature and core modules. The dependency graph below illustrates the relationships between modules.

![dependency-graph](docs/dependency-graph.png)

## Running the Expo app

1. Navigate to the `expo` directory.
2. Install dependencies with `npm install`.
3. Start the app with `npx expo start`.


