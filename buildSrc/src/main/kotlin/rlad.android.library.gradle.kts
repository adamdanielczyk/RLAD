plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    configureKotlinAndroid()
}

dependencies {
    implementation(Libs.Kotlin.stdlib)

    implementation(Libs.Hilt.hilt)
    implementation(Libs.Hilt.navigation)
    kapt(Libs.Hilt.compiler)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.AndroidX.Paging.paging)
}
