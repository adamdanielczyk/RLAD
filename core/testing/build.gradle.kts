plugins {
    id("rlad.android.library")
}

android {
    namespace = "com.rlad.core.testing"
}

dependencies {
    api(libs.test.coroutines)
    api(libs.test.junit)
    api(libs.test.mockk)
    api(libs.test.androidx.runner)
    api(libs.test.androidx.core)
    api(libs.test.androidx.paging)
}
