import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.shared(libs: VersionCatalog) {
    add("implementation", libs.findLibrary("kotlin.stdlib").get())

    add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())
    add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())

    add("implementation", libs.findLibrary("androidx.paging.runtime").get())
    add("implementation", libs.findLibrary("androidx.paging.compose").get())

    add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
    add("implementation", libs.findLibrary("androidx.navigation.compose").get())

    add("implementation", libs.findLibrary("androidx.activity").get())
    add("implementation", libs.findLibrary("androidx.activity.compose").get())

    add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
    add("implementation", libs.findLibrary("androidx.compose.ui").get())
    add("implementation", libs.findLibrary("androidx.compose.ui.tooling").get())
    add("implementation", libs.findLibrary("androidx.compose.foundation").get())
    add("implementation", libs.findLibrary("androidx.compose.material").get())
    add("implementation", libs.findLibrary("androidx.compose.material.icons").get())

    add("implementation", libs.findLibrary("androidx.lifecycle.runtime").get())
    add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel").get())
    add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())

    add("implementation", libs.findLibrary("accompanist.systemuicontroller").get())

    add("implementation", libs.findLibrary("coil.compose").get())
    add("implementation", libs.findLibrary("coil.gif").get())

    add("implementation", libs.findLibrary("lottie").get())
}