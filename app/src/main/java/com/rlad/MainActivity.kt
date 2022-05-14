package com.rlad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rlad.domain.navigation.Navigator
import com.rlad.ui.RladTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject lateinit var navigators: Set<@JvmSuppressWildcards Navigator>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RladApp()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun RladApp() = RladTheme {
        val navController = rememberNavController()

        val keyboardController = LocalSoftwareKeyboardController.current
        navController.addOnDestinationChangedListener { _, _, _ ->
            keyboardController?.hide()
        }

        NavHost(
            navController = navController,
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