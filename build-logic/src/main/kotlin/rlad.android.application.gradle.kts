plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.compose")
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
