import org.gradle.kotlin.dsl.kotlin

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("retrofit").get())
    implementation(libs.findLibrary("retrofit.converterMoshi").get())
    implementation(libs.findLibrary("moshi").get())
    kapt(libs.findLibrary("moshi.kotlinCodegen").get())
}
