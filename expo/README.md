# Expo app

Install dependencies with `npm ci`. Put `EXPO_PUBLIC_GIPHY_API_KEY` in `.env.local` as described in the repository README, then use `npm run android` or `npm run web`.

## Dependency updates

Expo SDK controls compatible React, React Native, native-module, and Android build-tool versions. Update the SDK first, run `npx expo install --fix`, and check `npx expo install --check`. Update independent libraries separately. Keep Babel, ESLint, and TypeScript within the ranges supported by the Expo toolchain and its plugins.

The generated `android/` project is committed and CI builds those committed sources. During an SDK upgrade, run `npx expo prebuild --platform android --clean --no-install` and review the generated changes. Preserve the existing debug keystore, launcher/splash assets, ARM architectures, release optimization settings, and unsigned release build (CI signs the APK/AAB). Prebuild restores debug signing for release by default, so remove that release signing assignment before committing.

Expo Doctor warns that app config fields are not automatically synced when native folders are committed. This is expected for this workflow: changes to native app configuration require an explicit prebuild and review before committing.

## Validation

- `npx expo install --check`
- `npm run typecheck`
- `npm run lint`
- `npx expo-doctor` (the committed-native-folder warning above is expected)
- `npx expo export --platform all`
- In `android/`: `./gradlew assembleDebug assembleRelease bundleRelease`

After a major SDK update, check a fresh app start, all data sources, search, grid/feed layouts, details, favorites persistence, and image sharing on an Android device or emulator.
