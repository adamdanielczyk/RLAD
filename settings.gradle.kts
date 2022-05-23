include(":app")
includeAll(directory = "features")
includeAll(directory = "libs")

fun includeAll(directory: String, moduleName: String = directory) {
    file(directory).listFiles()!!.forEach { file ->
        when {
            file.isDirectory -> includeAll(directory = file.path, moduleName = "$moduleName:${file.name}")
            file.name == "build.gradle" -> include(moduleName)
        }
    }
}