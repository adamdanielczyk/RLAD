package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

abstract class RenameBundleTask : DefaultTask() {

    @get:InputFiles
    abstract val bundleFile: RegularFileProperty

    @get:Input
    abstract val artifactName: Property<String>

    @TaskAction
    fun action() {
        val originalBundleFile = bundleFile.get().asFile

        originalBundleFile.renameTo(
            originalBundleFile.parentFile.resolve("${artifactName.get()}.aab"),
        )
    }
}