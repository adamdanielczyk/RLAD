plugins {
    id("rlad.android.library")
}

android {
    namespace = "com.rlad.testutils"
}

dependencies {
    api(libs.test.coroutines)
    api(libs.test.junit)
    api(libs.test.mockk)
    api(libs.test.androidx.runner)
    api(libs.test.androidx.core)
}