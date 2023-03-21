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
    implementation(libs().findLibrary("androidx.room").get())
    implementation(libs().findLibrary("androidx.room.paging").get())
    implementation(libs().findLibrary("androidx.room.runtime").get())
    kapt(libs().findLibrary("androidx.room.compiler").get())
}
