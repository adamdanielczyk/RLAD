plugins {
    id("com.android.library")
    kotlin("android")
    id("rlad.android.library.compose")
    id("rlad.android.hilt")
}

android {
    configureKotlinAndroid()
}

dependencies {
    shared()
}
