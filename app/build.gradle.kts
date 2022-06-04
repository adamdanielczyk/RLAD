plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    configureKotlinAndroid()
    configureCompose()

    defaultConfig {
        targetSdk = 31
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

    implementation(Libs.Kotlin.stdlib)

    implementation(Libs.Hilt.hilt)
    implementation(Libs.Hilt.navigation)
    kapt(Libs.Hilt.compiler)

    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.AndroidX.Paging.paging)
    implementation(Libs.AndroidX.Paging.compose)

    implementation(Libs.AndroidX.navigation)

    implementation(Libs.AndroidX.Activity.activity)
    implementation(Libs.AndroidX.Activity.compose)

    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.materialIcons)

    implementation(Libs.AndroidX.Lifecycle.ViewModel.viewmodel)
    implementation(Libs.AndroidX.Lifecycle.ViewModel.compose)
    implementation(Libs.AndroidX.Lifecycle.runtime)

    implementation(Libs.Accompanist.systemuicontroller)
    implementation(Libs.Accompanist.swiperefresh)

    implementation(Libs.Coil.compose)
    implementation(Libs.Coil.gif)

    implementation(Libs.Google.material)

    implementation(Libs.lottie)
}