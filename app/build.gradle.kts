plugins {
    id("rlad.android.application")
}

android {
    namespace = "com.rlad"

    defaultConfig {
        targetSdk = 36
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
    implementation(projects.core.domain)
    implementation(projects.core.infrastructure.common)
    implementation(projects.core.infrastructure.rickandmorty)
    implementation(projects.core.infrastructure.giphy)
    implementation(projects.core.infrastructure.artic)
    implementation(projects.core.ui)
    implementation(projects.feature.search)
    implementation(projects.feature.details)
}