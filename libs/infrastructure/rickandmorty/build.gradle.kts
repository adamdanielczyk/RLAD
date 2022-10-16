plugins {
    id("rlad.android.library")
    id("rlad.android.room")
    id("rlad.android.retrofit")
}

android {
    namespace = "com.rlad.infrastructure.rickandmorty"
}

dependencies {
    implementation(projects.libs.domain)
    implementation(projects.libs.infrastructure.common)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)
}