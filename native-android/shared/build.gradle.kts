plugins {
    id("rlad.kmp.library")
    id("rlad.kmp.ktor")
    id("rlad.kmp.koin")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.test.coroutines)
        }
    }
}

android {
    namespace = "com.rlad.shared"
}