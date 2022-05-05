package com.rlad.domain.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

interface Navigator {
    val route: String
    val isStartDestination: Boolean
        get() = false

    @Composable
    fun Content(navController: NavHostController)
}

interface DetailsNavigator : Navigator {
    fun navigate(navController: NavHostController, id: String)
}