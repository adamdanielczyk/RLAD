package com.sample.features.details.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.sample.core.navigation.DetailsNavigator
import com.sample.features.details.ui.DetailScreen
import javax.inject.Inject

class DetailsNavigatorImpl @Inject constructor() : DetailsNavigator {

    override val route: String
        get() = "details/{id}"

    override fun navigate(navController: NavHostController, id: Int) {
        navController.navigate("details/$id")
    }

    @Composable
    override fun Content() {
        DetailScreen()
    }
}