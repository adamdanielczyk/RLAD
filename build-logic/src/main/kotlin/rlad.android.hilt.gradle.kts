plugins {
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    add("implementation", libs().findLibrary("hilt.android").get())
    add("kapt", libs().findLibrary("hilt.compiler").get())
}
