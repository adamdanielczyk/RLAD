package tasks

import com.android.build.api.variant.BuiltArtifactsLoader
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class RenameApkTask : DefaultTask() {

    @get:InputFiles
    abstract val apkDirectory: DirectoryProperty

    @get:Internal
    abstract val builtArtifactsLoader: Property<BuiltArtifactsLoader>

    @get:Input
    abstract val artifactName: Property<String>

    @TaskAction
    fun action() {
        val builtArtifacts = builtArtifactsLoader.get().load(apkDirectory.get()) ?: error("Cannot load APKs")

        val originalFile = File(builtArtifacts.elements.single().outputFile)

        originalFile.renameTo(
            originalFile.parentFile.resolve("${artifactName.get()}.apk"),
        )
    }
}
