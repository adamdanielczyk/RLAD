plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("androidx.room")
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
