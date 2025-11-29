plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.kotlin.compose)
    implementation(libs.gradlePlugin.kotlin.serialization)
    implementation(libs.gradlePlugin.ksp)
    implementation(libs.gradlePlugin.metro)
    implementation(libs.gradlePlugin.room)

    // workaround to enable version catalog in build-logic
    // see https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
