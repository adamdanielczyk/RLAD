package com.rlad.shared.domain.navigation

actual interface Navigator {
    actual val route: String
    actual val isStartDestination: Boolean
        get() = false
}

actual interface DetailsNavigator : Navigator {
    actual fun navigate(id: String)
}