plugins {
    id("rlad.android.library")
    id("rlad.android.room")
}

android {
    namespace = "com.rlad.infrastructure.common"
}

dependencies {
    implementation(projects.libs.domain)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)
}