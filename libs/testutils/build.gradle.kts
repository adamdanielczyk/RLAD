plugins {
    id("rlad.android.library")
    id("dagger.hilt.android.plugin")
}

dependencies {
    api(libs.test.coroutines)
    api(libs.test.junit)
    api(libs.test.mockk)
    api(libs.test.androidx.runner)
    api(libs.test.androidx.core)
}