plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dev.zacsweers.metro")
//    id("rlad.android.metro")
}

android {
    configureKotlinAndroid()
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.lifecycle.viewmodel)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
