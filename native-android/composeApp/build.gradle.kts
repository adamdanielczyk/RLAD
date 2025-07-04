plugins {
    id("rlad.kmp.compose")
    id("rlad.kmp.koin")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared"))
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.androidx.paging.common)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.network.ktor)
            api("app.cash.paging:paging-compose-common:3.3.0-alpha02-0.5.1")
        }
        
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.coil.compose)
            implementation(libs.androidx.paging.compose)
        }
    }
}

android {
    namespace = "com.rlad.composeapp"
}