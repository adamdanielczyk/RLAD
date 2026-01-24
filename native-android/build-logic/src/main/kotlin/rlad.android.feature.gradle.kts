import com.android.build.api.dsl.LibraryExtension

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("rlad.android.library.compose")
    id("rlad.android.metro")
}

extensions.configure<LibraryExtension> {
    configureKotlinAndroid(this)
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))

    shared()
}
