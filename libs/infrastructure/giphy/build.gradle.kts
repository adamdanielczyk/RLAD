plugins {
    id("rlad.android.library")
    id("rlad.android.room")
    id("rlad.android.retrofit")
}

android {
    namespace = "com.rlad.infrastructure.giphy"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val giphyApiKey = project.property("GIPHY_API_KEY")
        buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")
    }
}

dependencies {
    implementation(projects.libs.domain)
    implementation(projects.libs.infrastructure.common)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)
}