package com.rlad

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.rlad.core.ui.RladTheme
import com.rlad.core.ui.navigation.Navigator
import com.rlad.feature.search.navigation.SearchRoute
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlin.reflect.KClass

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey(MainActivity::class)
class MainActivity(
    private val metroViewModelFactory: MetroViewModelFactory,
    private val navigators: Map<KClass<*>, Navigator<NavKey>>,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(LocalMetroViewModelFactory provides metroViewModelFactory) {
                val useDarkTheme = isSystemInDarkTheme()

                RladTheme(useDarkTheme = useDarkTheme) {
                    val backStack = rememberNavBackStack(SearchRoute)

                    NavDisplay(
                        backStack = backStack,
                    ) { route ->
                        NavEntry(route) {
                            navigators.getValue(route::class).Content(backStack, route)
                        }
                    }
                }
            }
        }
    }
}
