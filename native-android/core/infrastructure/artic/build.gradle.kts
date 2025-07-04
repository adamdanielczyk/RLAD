plugins {
    id("rlad.android.library")
    id("rlad.android.ktor")
    id("rlad.android.room")
}

android {
    namespace = "com.rlad.core.infrastructure.artic"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.infrastructure.common)
}
