package com.rlad.shared.domain.navigation

// Note: Platform-specific navigation will be implemented in platform modules
// This is the common interface for navigation
expect interface Navigator {
    val route: String
    val isStartDestination: Boolean
}

expect interface DetailsNavigator : Navigator {
    fun navigate(id: String)
}