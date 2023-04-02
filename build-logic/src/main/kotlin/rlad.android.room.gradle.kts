plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}
