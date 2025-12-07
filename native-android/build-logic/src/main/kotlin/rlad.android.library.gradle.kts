plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("rlad.android.metro")
}

android {
    configureKotlinAndroid()
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
