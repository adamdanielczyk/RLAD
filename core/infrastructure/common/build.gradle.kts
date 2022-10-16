plugins {
    id("rlad.android.library")
    id("rlad.android.room")
}

android {
    namespace = "com.rlad.core.infrastructure.common"
}

dependencies {
    implementation(projects.core.domain)

    testImplementation(projects.core.testing)
    androidTestImplementation(projects.core.testing)
}