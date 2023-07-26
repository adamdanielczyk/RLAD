import com.android.build.api.dsl.CommonExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal val Project.libs
    get() = the<LibrariesForLibs>()

internal fun Project.configureKotlinAndroid() {
    val javaVersion = JavaVersion.VERSION_17

    kotlin {
        jvmToolchain(javaVersion.majorVersion.toInt())
    }

    android {
        compileSdk = 33

        defaultConfig {
            minSdk = 26
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            testInstrumentationRunnerArguments["clearPackageData"] = "true"
        }

        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=androidx.paging.ExperimentalPagingApi",
            )
        }

        packaging {
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
    android {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            )
        }

        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
        }
    }
}

private fun Project.android(action: CommonExtension<*, *, *, *, *>.() -> Unit) {
    (this as ExtensionAware).extensions.configure("android", action)
}

private fun Project.kotlin(action: KotlinAndroidProjectExtension.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlin", action)
}

private fun CommonExtension<*, *, *, *, *>.kotlinOptions(action: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", action)
}