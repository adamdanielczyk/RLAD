plugins {
    id("rlad.android.library")
    id("rlad.android.ktor")
    id("rlad.android.room")
}

android {
    namespace = "com.rlad.core.infrastructure.giphy"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val giphyApiKey = project.property("GIPHY_API_KEY")
        buildConfigField("String", "GIPHY_API_KEY", "\"$giphyApiKey\"")
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.infrastructure.common)
}
