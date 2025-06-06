plugins {
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

dependencies {
    add("implementation", libs.hilt.android)
    add("ksp", libs.hilt.compiler)
}
