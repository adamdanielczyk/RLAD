plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    configureKotlinAndroid()
    configureCompose(this)

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
