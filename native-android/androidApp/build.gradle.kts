plugins {
    id("rlad.android.application")
}

android {
    namespace = "com.rlad.androidapp"
    
    defaultConfig {
        applicationId = "com.rlad.androidapp"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(project(":shared"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose.viewmodel)
}