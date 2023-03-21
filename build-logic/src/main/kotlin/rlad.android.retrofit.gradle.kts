plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(libs().findLibrary("retrofit").get())
    implementation(libs().findLibrary("retrofit.converterMoshi").get())
    implementation(libs().findLibrary("moshi").get())
    kapt(libs().findLibrary("moshi.kotlinCodegen").get())
}
