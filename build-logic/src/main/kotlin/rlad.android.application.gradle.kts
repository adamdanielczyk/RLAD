plugins {
    id("com.android.application")
    kotlin("android")
    id("rlad.android.hilt")
    id("rlad.renameartifacts")
}

android {
    configureKotlinAndroid()
    configureCompose()
}

dependencies {
    shared()
}
