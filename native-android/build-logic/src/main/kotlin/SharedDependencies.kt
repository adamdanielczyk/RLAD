import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.shared() {
    dependencies {
        add("implementation", libs.androidx.activity)
        add("implementation", libs.androidx.activity.compose)
        add("implementation", platform(libs.androidx.compose.bom))
        add("implementation", libs.androidx.compose.foundation)
        add("implementation", libs.androidx.compose.material)
        add("implementation", libs.androidx.compose.material.icons)
        add("implementation", libs.androidx.compose.ui)
        add("implementation", libs.androidx.compose.ui.tooling)
        add("implementation", libs.androidx.lifecycle.runtime)
        add("implementation", libs.androidx.lifecycle.viewmodel)
        add("implementation", libs.androidx.lifecycle.viewmodel.compose)
        add("implementation", libs.androidx.navigation)
        add("implementation", libs.androidx.paging.compose)
        add("implementation", libs.androidx.paging.runtime)
        add("implementation", libs.coil.compose)
        add("implementation", libs.coil.gif)
        add("implementation", libs.kotlinx.coroutines.android)
        add("implementation", libs.kotlinx.coroutines.core)
        add("implementation", libs.lottie)
        add("implementation", libs.metro.android)
        add("implementation", libs.metro.viewmodel)

        add("testImplementation", project(":core:testing"))
        add("androidTestImplementation", project(":core:testing"))
    }
}
