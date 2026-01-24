import com.android.build.api.dsl.LibraryExtension

plugins {
    id("com.android.library")
    id("rlad.android.metro")
}

extensions.configure<LibraryExtension> {
    configureKotlinAndroid(this)
}

dependencies {
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
