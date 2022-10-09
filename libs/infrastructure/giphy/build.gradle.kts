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

        val giphyApiKey = project.property("GIPHY_API_KEY")
        buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")
    }
}

dependencies {
    implementation(projects.libs.domain)
    implementation(projects.libs.infrastructure.common)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)

    implementation(libs.androidx.room)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converterMoshi)
    implementation(libs.moshi)
    kapt(libs.moshi.kotlinCodegen)
}