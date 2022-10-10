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
    implementation(project(":libs:domain"))
    implementation(project(":libs:ui"))

    testImplementation(project(":libs:testutils"))
    androidTestImplementation(project(":libs:testutils"))

    shared()
}
