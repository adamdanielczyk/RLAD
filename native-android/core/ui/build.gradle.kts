plugins {
    id("rlad.android.library")
    id("rlad.android.library.compose")
}

android {
    namespace = "com.rlad.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
}