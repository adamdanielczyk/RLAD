package com.rlad.core.ui.di

import com.rlad.core.domain.navigation.Navigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds

@DependencyGraph(AppScope::class)
interface AppGraph {
    @Multibinds
    val navigators: Set<Navigator>
}
