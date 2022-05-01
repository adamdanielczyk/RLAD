package com.sample.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface Navigator {
    val route: String
}

interface SearchNavigator : Navigator {
    @Composable
    fun Content(openDetails: (Int) -> Unit)
}

interface DetailsNavigator : Navigator {
    fun navigate(navController: NavHostController, id: Int)

    @Composable
    fun Content()
}