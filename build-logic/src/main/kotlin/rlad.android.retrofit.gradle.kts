plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converterMoshi)
    implementation(libs.moshi)
    kapt(libs.moshi.kotlinCodegen)
}
