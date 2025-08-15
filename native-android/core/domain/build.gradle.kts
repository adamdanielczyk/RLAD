plugins {
    id("rlad.android.library")
    id("rlad.android.library.compose")
}

android {
    namespace = "com.rlad.core.domain"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}
