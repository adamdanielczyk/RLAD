plugins {
    id("rlad.android.application")
}

android {
    defaultConfig {
        targetSdk = 33
        applicationId = "com.rlad"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.libs.domain)
    implementation(projects.libs.infrastructure.common)
    implementation(projects.libs.infrastructure.rickandmorty)
    implementation(projects.libs.infrastructure.giphy)
    implementation(projects.libs.ui)
    implementation(projects.features.search)
    implementation(projects.features.details)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)
}