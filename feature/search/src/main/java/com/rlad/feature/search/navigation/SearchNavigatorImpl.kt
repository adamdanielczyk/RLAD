package com.rlad.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.rlad.core.domain.navigation.DetailsNavigator
import com.rlad.core.domain.navigation.Navigator
import com.rlad.feature.search.ui.SearchScreen
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
            onItemCardClicked = { id -> detailsNavigator.navigate(navController, id) },
        )
    }
}
