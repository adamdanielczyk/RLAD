package com.rlad

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rlad.core.domain.navigation.Navigator
import com.rlad.core.ui.RladTheme
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey(MainActivity::class)
@Inject
class MainActivity(
    private val metroViewModelFactory: MetroViewModelFactory,
    private val navigators: Set<Navigator>,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(LocalMetroViewModelFactory provides metroViewModelFactory) {
                val useDarkTheme = isSystemInDarkTheme()

                RladTheme(useDarkTheme = useDarkTheme) {
                    val navController = rememberNavController()

                    val keyboardController = LocalSoftwareKeyboardController.current
                    navController.addOnDestinationChangedListener { _, _, _ ->
                        keyboardController?.hide()
                    }

                    NavHost(
                        navController = navController,
                        contentAlignment = Alignment.TopCenter,
                        startDestination = navigators.first(Navigator::isStartDestination).route,
                    ) {
                        navigators.forEach { navigator ->
                            composable(navigator.route) {
                                navigator.Content(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
