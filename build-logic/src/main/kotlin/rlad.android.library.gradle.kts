plugins {
    id("com.android.library")
    kotlin("android")
    id("rlad.android.hilt")
}

android {
    configureKotlinAndroid()
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel)
}
