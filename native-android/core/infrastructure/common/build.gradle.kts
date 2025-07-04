plugins {
    id("rlad.android.library")
    id("rlad.android.ktor")
    id("rlad.android.room")
}

android {
    namespace = "com.rlad.core.infrastructure.common"
}

dependencies {
    implementation(projects.core.domain)
}
