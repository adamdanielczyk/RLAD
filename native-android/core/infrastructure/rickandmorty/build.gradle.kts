plugins {
    id("rlad.android.library")
    id("rlad.android.room")
    id("rlad.android.retrofit")
}

android {
    namespace = "com.rlad.core.infrastructure.rickandmorty"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.infrastructure.common)
}
