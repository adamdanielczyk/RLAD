import com.android.build.api.dsl.CommonExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal val Project.libs
    get() = the<LibrariesForLibs>()

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    extensions.configure<KotlinAndroidProjectExtension> {
        jvmToolchain(JavaVersion.VERSION_17.majorVersion.toInt())

        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=androidx.paging.ExperimentalPagingApi",
                "-Xannotation-default-target=param-property",
            )
        }
    }

    commonExtension.apply {
        compileSdk = 36

        defaultConfig.apply {
            minSdk = 28
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            testInstrumentationRunnerArguments["clearPackageData"] = "true"
        }

        packaging.resources {
            excludes += listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
            )
        }
    }
}

internal fun Project.configureCompose(
    commonExtension: CommonExtension,
) {
    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            )
        }
    }

    commonExtension.apply {
        buildFeatures.compose = true
    }
}
