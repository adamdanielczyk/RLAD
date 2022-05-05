package com.rlad.features.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.rlad.domain.navigation.DetailsNavigator
import com.rlad.domain.navigation.Navigator
import com.rlad.features.search.ui.SearchScreen
import javax.inject.Inject

internal class SearchNavigatorImpl @Inject constructor(
    private val detailsNavigator: DetailsNavigator,
) : Navigator {

    override val isStartDestination: Boolean
        get() = true

    override val route: String
        get() = "search"

    @Composable
    override fun Content(navController: NavHostController) {
        SearchScreen(
            openDetails = { id -> detailsNavigator.navigate(navController, id) }
        )
    }
}