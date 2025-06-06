plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("rlad.android.hilt")
}

android {
    configureKotlinAndroid()
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
