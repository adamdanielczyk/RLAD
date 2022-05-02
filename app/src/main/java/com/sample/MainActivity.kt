package com.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sample.domain.navigation.DetailsNavigator
import com.sample.domain.navigation.SearchNavigator
import com.sample.ui.SampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var searchNavigator: SearchNavigator
    @Inject lateinit var detailsNavigator: DetailsNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampleApp(searchNavigator, detailsNavigator)
        }
    }
}

@Composable
fun SampleApp(searchNavigator: SearchNavigator, detailsNavigator: DetailsNavigator) {
    SampleTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = searchNavigator.route,
        ) {
            composable(searchNavigator.route) {
                searchNavigator.Content(
                    openDetails = { id -> detailsNavigator.navigate(navController, id) }
                )
            }
            composable(detailsNavigator.route) {
                detailsNavigator.Content()
            }
        }
    }
}