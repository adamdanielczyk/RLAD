plugins {
    id("rlad.android.feature")
}

dependencies {
    implementation(projects.libs.domain)
    implementation(projects.libs.ui)

    testImplementation(projects.libs.testutils)
    androidTestImplementation(projects.libs.testutils)
}