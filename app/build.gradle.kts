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

    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.swiperefresh)

    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.android.material)

    implementation(libs.lottie)
}