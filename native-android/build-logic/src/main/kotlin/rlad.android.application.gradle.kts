plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
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
