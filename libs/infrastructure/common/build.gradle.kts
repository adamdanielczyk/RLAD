plugins {
    id("rlad.android.library")
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
    implementation(projects.libs.domain)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)

    implementation(libs.androidx.room)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}