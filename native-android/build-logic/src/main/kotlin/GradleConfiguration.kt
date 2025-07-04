import com.android.build.gradle.BaseExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

object GradleConfiguration {
    const val compileSdk = 36
    const val minSdk = 26
}

internal val Project.libs
    get() = the<LibrariesForLibs>()

internal fun Project.configureKotlinAndroid() {
    val javaVersion = JavaVersion.VERSION_17

    kotlin {
        jvmToolchain(javaVersion.majorVersion.toInt())

        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=androidx.paging.ExperimentalPagingApi",
            )
        }
    }

    android {
        compileSdkVersion(36)

        defaultConfig {
            minSdk = 26
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            testInstrumentationRunnerArguments["clearPackageData"] = "true"
        }

        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        packagingOptions {
            resources {
                excludes += listOf(
                    "META-INF/LICENSE.md",
                    "META-INF/LICENSE-notice.md",
                )
            }
        }
    }
}

internal fun Project.configureCompose() {
    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            )
        }
    }

    android {
        buildFeatures.compose = true
    }
}

private fun Project.android(action: BaseExtension.() -> Unit) {
    (this as ExtensionAware).extensions.configure("android", action)
}

private fun Project.kotlin(action: KotlinAndroidProjectExtension.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlin", action)
}
