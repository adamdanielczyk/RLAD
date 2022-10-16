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
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

    shared()
}
