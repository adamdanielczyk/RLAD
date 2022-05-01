package com.sample.features.search.navigation

import androidx.compose.runtime.Composable
import com.sample.core.navigation.SearchNavigator
import com.sample.features.search.ui.SearchScreen
import javax.inject.Inject

class SearchNavigatorImpl @Inject constructor() : SearchNavigator {

    override val route: String
        get() = "search"

    @Composable
    override fun Content(openDetails: (Int) -> Unit) {
        SearchScreen(openDetails)
    }
}