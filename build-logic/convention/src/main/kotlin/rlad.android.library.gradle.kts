plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    configureKotlinAndroid()
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("kotlin.stdlib").get())

    implementation(libs.findLibrary("hilt.android").get())
    kapt(libs.findLibrary("hilt.compiler").get())

    implementation(libs.findLibrary("kotlinx.coroutines.core").get())
    implementation(libs.findLibrary("kotlinx.coroutines.android").get())

    implementation(libs.findLibrary("androidx.paging.runtime").get())

    implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())

    implementation(libs.findLibrary("androidx.lifecycle.viewmodel").get())
}
