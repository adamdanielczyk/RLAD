plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    configureCompose(this)
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(platform(libs.findLibrary("androidx.compose.bom").get()))
    implementation(libs.findLibrary("androidx.compose.ui").get())
    implementation(libs.findLibrary("androidx.compose.ui.tooling").get())
    implementation(libs.findLibrary("androidx.compose.foundation").get())
    implementation(libs.findLibrary("androidx.compose.material").get())
    implementation(libs.findLibrary("androidx.compose.material.icons").get())
}
