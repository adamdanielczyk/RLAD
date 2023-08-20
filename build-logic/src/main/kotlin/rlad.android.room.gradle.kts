plugins {
    id("com.android.library")
    kotlin("android")
    id("com.google.devtools.ksp")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
}

dependencies {
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
