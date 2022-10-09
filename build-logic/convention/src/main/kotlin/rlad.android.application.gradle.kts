plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    configureKotlinAndroid()
    configureCompose(this)
}
