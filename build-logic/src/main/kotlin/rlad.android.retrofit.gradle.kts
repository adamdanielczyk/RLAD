plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converterMoshi)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlinCodegen)
}
