plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("dev.zacsweers.metro")
//    id("rlad.android.metro")
    id("rlad.renameartifacts")
}

android {
    configureKotlinAndroid()
    configureCompose()
}

dependencies {
    shared()
}
