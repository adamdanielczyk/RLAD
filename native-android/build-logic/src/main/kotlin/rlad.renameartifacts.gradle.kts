import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.VariantOutputConfiguration
import tasks.RenameApkTask
import tasks.RenameBundleTask

plugins.withId("com.android.application") {
    if (!System.getenv("CI").toBoolean()) return@withId

    project.extensions.configure<ApplicationAndroidComponentsExtension> {
        onVariants { variant ->
            val variantName = variant.name
            val variantNameCapitalized = variant.name.replaceFirstChar { it.uppercase() }

            val mainOutput = variant.outputs.single { it.outputType == VariantOutputConfiguration.OutputType.SINGLE }

            val artifactName = mainOutput.versionName.zip(mainOutput.versionCode) { versionName, versionCode ->
                getArtifactName(variantName, versionName, versionCode)
            }

            val renameApkTask = project.tasks.register<RenameApkTask>("rename${variantNameCapitalized}Apk") {
                this.apkDirectory.set(variant.artifacts.get(SingleArtifact.APK))
                this.builtArtifactsLoader.set(variant.artifacts.getBuiltArtifactsLoader())
                this.artifactName.set(artifactName)
            }

            val renameBundleTask = project.tasks.register<RenameBundleTask>("rename${variantNameCapitalized}Bundle") {
                this.bundleFile.set(variant.artifacts.get(SingleArtifact.BUNDLE))
                this.artifactName.set(artifactName)
            }

            project.afterEvaluate {
                project.tasks.findByName("bundle$variantNameCapitalized")?.finalizedBy(renameBundleTask)
                project.tasks.findByName("assemble$variantNameCapitalized")?.finalizedBy(renameApkTask)
            }
        }
    }
}

fun getArtifactName(variantName: String, versionName: String?, versionCode: Int?): String = buildString {
    append(project.rootProject.name.lowercase())
    append("-$variantName")
    append("-vn$versionName")
    append("-vc$versionCode")
}
