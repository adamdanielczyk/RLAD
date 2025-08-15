pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "rlad"
include(":app")
includeAll(directory = "feature")
includeAll(directory = "core")

fun includeAll(directory: String, moduleName: String = directory) {
    file(directory).listFiles()!!.forEach { file ->
        when {
            file.isDirectory -> includeAll(directory = file.path, moduleName = "$moduleName:${file.name}")
            file.name == "build.gradle.kts" -> include(moduleName)
        }
    }
}
