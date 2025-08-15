package com.rlad.feature.details.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.rlad.core.domain.navigation.DetailsNavigator
import com.rlad.core.domain.navigation.Navigator
import com.rlad.feature.details.ui.DetailsScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@ContributesIntoSet(AppScope::class)
@Inject
class DetailsNavigatorImpl : DetailsNavigator {

    override val route: String
        get() = "details/{id}"

    override fun navigate(navController: NavHostController, id: String) {
        navController.navigate("details/$id")
    }

    @Composable
    override fun Content(navController: NavHostController) {
        DetailsScreen()
    }
}
