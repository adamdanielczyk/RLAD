plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("rlad.android.library.compose")
    id("rlad.android.metro")
}

android {
    configureKotlinAndroid()
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))

    shared()
}
