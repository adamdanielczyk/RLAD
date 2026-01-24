import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.shared() {
    dependencies {
        "implementation"(libs.androidx.activity)
        "implementation"(libs.androidx.activity.compose)
        "implementation"(platform(libs.androidx.compose.bom))
        "implementation"(libs.androidx.compose.foundation)
        "implementation"(libs.androidx.compose.material)
        "implementation"(libs.androidx.compose.material.icons)
        "implementation"(libs.androidx.compose.ui)
        "implementation"(libs.androidx.compose.ui.tooling)
        "implementation"(libs.androidx.lifecycle.runtime)
        "implementation"(libs.androidx.lifecycle.viewmodel)
        "implementation"(libs.androidx.lifecycle.viewmodel.compose)
        "implementation"(libs.androidx.navigation)
        "implementation"(libs.androidx.paging.compose)
        "implementation"(libs.androidx.paging.runtime)
        "implementation"(libs.coil.compose)
        "implementation"(libs.coil.gif)
        "implementation"(libs.kotlinx.coroutines.android)
        "implementation"(libs.kotlinx.coroutines.core)
        "implementation"(libs.lottie)
        "implementation"(libs.metro.android)
        "implementation"(libs.metro.viewmodel)

        "testImplementation"(project(":core:testing"))
        "androidTestImplementation"(project(":core:testing"))
    }
}
