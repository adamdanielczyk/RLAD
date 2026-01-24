package com.rlad.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.rlad.core.ui.navigation.DetailsNavigator
import com.rlad.core.ui.navigation.Navigator
import com.rlad.feature.search.ui.SearchScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ClassKey
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import kotlinx.serialization.Serializable

@ContributesIntoMap(AppScope::class, binding<Navigator<NavKey>>())
@ClassKey(SearchRoute::class)
class SearchNavigatorImpl(
    private val detailsNavigator: DetailsNavigator<*>,
) : Navigator<SearchRoute> {

    @Composable
    override fun Content(backStack: MutableList<NavKey>, route: SearchRoute) {
        SearchScreen(
            onItemCardClicked = { id -> detailsNavigator.navigate(backStack, id) },
        )
    }
}

@Serializable
data object SearchRoute : NavKey
