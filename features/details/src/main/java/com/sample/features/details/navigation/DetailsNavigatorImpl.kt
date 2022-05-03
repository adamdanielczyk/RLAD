package com.sample.features.details.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sample.domain.navigation.DetailsNavigator
import com.sample.features.details.ui.DetailScreen
import javax.inject.Inject

internal class DetailsNavigatorImpl @Inject constructor() : DetailsNavigator {

    override val route: String
        get() = "details/{id}"

    override fun navigate(navController: NavHostController, id: String) {
        navController.navigate("details/$id")
    }

    @Composable
    override fun Content(navController: NavHostController) {
        DetailScreen()
    }
}