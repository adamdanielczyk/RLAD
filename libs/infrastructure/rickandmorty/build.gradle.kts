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
    }
}

dependencies {
    implementation(project(":libs:domain"))
    implementation(project(":libs:infrastructure:common"))

    testImplementation(project(":libs:testutils"))
    androidTestImplementation(project(":libs:testutils"))

    implementation(Libs.AndroidX.Room.paging)
    implementation(Libs.AndroidX.Room.room)
    implementation(Libs.AndroidX.Room.runtime)
    kapt(Libs.AndroidX.Room.compiler)

    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.moshiConverter)
    implementation(Libs.Moshi.moshi)
    kapt(Libs.Moshi.kapt)
}