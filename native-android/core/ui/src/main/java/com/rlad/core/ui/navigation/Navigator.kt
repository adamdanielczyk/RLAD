package com.rlad.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

interface Navigator<N : NavKey> {
    @Composable
    fun Content(backStack: MutableList<NavKey>, route: N)
}

interface DetailsNavigator<N : NavKey> : Navigator<N> {
    fun navigate(backStack: MutableList<NavKey>, id: String)
}
