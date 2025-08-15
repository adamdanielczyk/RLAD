package com.rlad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rlad.core.domain.navigation.Navigator
import com.rlad.core.ui.RladTheme
import com.rlad.core.ui.viewmodel.MetroViewModelFactory
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    private val navigators: Set<Navigator> by lazy { 
        (application as RladApplication).appGraph.navigators 
    }
    
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = MetroViewModelFactory((application as RladApplication).appGraph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
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
