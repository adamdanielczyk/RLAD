package com.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sample.domain.navigation.Navigator
import com.sample.ui.SampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var navigators: Set<@JvmSuppressWildcards Navigator>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampleApp()
        }
    }

    @Composable
    private fun SampleApp() = SampleTheme {
        val navController = rememberNavController()

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