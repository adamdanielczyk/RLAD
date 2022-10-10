plugins {
    id("com.android.application")
    kotlin("android")
    id("rlad.android.hilt")
}

android {
    configureKotlinAndroid()
    configureCompose(this)
}

dependencies {
    shared()
}
