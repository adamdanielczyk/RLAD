plugins {
    id("com.android.library")
    kotlin("android")
    id("rlad.android.library.compose")
    id("rlad.android.hilt")
}

android {
    configureKotlinAndroid()
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("kotlin.stdlib").get())

    implementation(libs.findLibrary("kotlinx.coroutines.core").get())
    implementation(libs.findLibrary("kotlinx.coroutines.android").get())

    implementation(libs.findLibrary("androidx.paging.runtime").get())
    implementation(libs.findLibrary("androidx.paging.compose").get())

    implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())
    implementation(libs.findLibrary("androidx.navigation.compose").get())

    implementation(libs.findLibrary("androidx.activity").get())
    implementation(libs.findLibrary("androidx.activity.compose").get())

    implementation(libs.findLibrary("androidx.lifecycle.runtime").get())
    implementation(libs.findLibrary("androidx.lifecycle.viewmodel").get())
    implementation(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())

    implementation(libs.findLibrary("accompanist.systemuicontroller").get())
    implementation(libs.findLibrary("accompanist.swiperefresh").get())

    implementation(libs.findLibrary("coil.compose").get())
    implementation(libs.findLibrary("coil.gif").get())

    implementation(libs.findLibrary("android.material").get())

    implementation(libs.findLibrary("lottie").get())
}
