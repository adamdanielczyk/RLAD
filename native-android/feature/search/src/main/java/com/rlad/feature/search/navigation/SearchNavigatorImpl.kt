package com.rlad.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.rlad.core.domain.navigation.DetailsNavigator
import com.rlad.core.domain.navigation.Navigator
import com.rlad.feature.search.ui.SearchScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding

@Inject
@ContributesIntoSet(AppScope::class, binding<Navigator>())
class SearchNavigatorImpl(
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
