package com.rlad.feature.details.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.rlad.core.ui.navigation.DetailsNavigator
import com.rlad.core.ui.navigation.Navigator
import com.rlad.feature.details.ui.DetailsScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ClassKey
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import kotlinx.serialization.Serializable

@ContributesBinding(AppScope::class, binding<DetailsNavigator<*>>())
@ContributesIntoMap(AppScope::class, binding<Navigator<NavKey>>())
@ClassKey(DetailsRoute::class)
class DetailsNavigatorImpl : DetailsNavigator<DetailsRoute> {

    override fun navigate(backStack: MutableList<NavKey>, id: String) {
        backStack.add(DetailsRoute(id))
    }

    @Composable
    override fun Content(backStack: MutableList<NavKey>, route: DetailsRoute) {
        DetailsScreen(id = route.id)
    }
}

@Serializable
data class DetailsRoute(val id: String) : NavKey
