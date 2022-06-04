enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
includeAll(directory = "features")
includeAll(directory = "libs")

fun includeAll(directory: String, moduleName: String = directory) {
    file(directory).listFiles()!!.forEach { file ->
        when {
            file.isDirectory -> includeAll(directory = file.path, moduleName = "$moduleName:${file.name}")
            file.name == "build.gradle.kts" -> include(moduleName)
        }
    }
}