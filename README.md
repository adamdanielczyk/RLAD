# RLAD - Random List And Details

RLAD is a playground for exploring mobile development patterns across different platforms. This repository contains two implementations of the same app:

- **[expo](expo)** – Cross-platform React Native app built with Expo
- **[native-android](native-android)** – Native Android app built with Kotlin, Jetpack Compose, and modern Android libraries

Both apps demonstrate fetching data from public APIs, displaying lists with search functionality, and navigating to detailed views.

## Download Links

- **Expo:** [Google Play](https://play.google.com/store/apps/details?id=com.rlad.expo)
- **Native Android:** [Google Play](https://play.google.com/store/apps/details?id=com.rlad)

## Architecture Overview

### Expo

- **Architecture:** Component-based with custom hooks
- **UI:** React Native with Expo Router for navigation
- **State Management:** Zustand + TanStack Query for server state
- **Networking:** Fetch API with TanStack Query
- **Storage:** AsyncStorage for local data persistence

### Native Android

- **Architecture:** MVVM with Clean Architecture principles
- **UI:** Jetpack Compose with Material Design 3
- **Dependency Injection:** Dagger Hilt
- **Networking:** Retrofit + OkHttp
- **State Management:** StateFlow + Compose State
- **Modularization:** Feature-based modules with shared core libraries

## APIs

- **[Art Institute of Chicago](https://api.artic.edu)** - Museum artwork and artifacts
- **[GIPHY](https://developers.giphy.com)** - GIF search and trending content
- **[Rick and Morty](https://rickandmortyapi.com)** - Character and episode data

## Setup Requirements

### API Key Configuration

Get a GIPHY API key from [developers.giphy.com](https://developers.giphy.com), then configure it for your target platform:

#### For Expo

Create a `.env.local` file in the `expo/` directory:

```bash
EXPO_PUBLIC_GIPHY_API_KEY=your_api_key_here
```

#### For Native Android

Add to `~/.gradle/gradle.properties`:

```bash
GIPHY_API_KEY=your_api_key_here
```

## Running the Apps

### Debug builds

#### Expo

```bash
# Navigate to expo directory
cd expo

# Install dependencies
npm install

# Run on specific platforms
# Launch on Android device/emulator
npm run android
# Launch on iOS device/simulator
npm run ios
# Launch in web browser
npm run web
```

#### Native Android

```bash
# Open in Android Studio
# File → Open → Select 'native-android' folder
# Build and run the project
```

### Production builds

#### Expo

```bash
cd expo
npx expo run:android --variant release

# OR

npx expo export --platform android
cd android
./gradlew assembleRelease
```

#### Native Android

```bash
cd native-android
./gradlew assembleRelease
```
