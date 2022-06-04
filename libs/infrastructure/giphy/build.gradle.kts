plugins {
    id("rlad.android.library")
    id("dagger.hilt.android.plugin")
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

    implementation(Libs.AndroidX.Room.paging)
    implementation(Libs.AndroidX.Room.room)
    implementation(Libs.AndroidX.Room.runtime)
    kapt(Libs.AndroidX.Room.compiler)

    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.moshiConverter)
    implementation(Libs.Moshi.moshi)
    kapt(Libs.Moshi.kapt)
}