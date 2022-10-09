plugins {
    id("rlad.android.library")
    id("rlad.android.room")
}

dependencies {
    implementation(projects.libs.domain)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)
}