plugins {
    id("rlad.android.library")
    id("rlad.android.library.compose")
}

android {
    namespace = "com.rlad.core.ui"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
}
