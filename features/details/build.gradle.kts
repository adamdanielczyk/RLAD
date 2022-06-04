plugins {
    id("rlad.android.feature")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":libs:domain"))
    implementation(project(":libs:ui"))

    testImplementation(project(":libs:testutils"))
    androidTestImplementation(project(":libs:testutils"))
}