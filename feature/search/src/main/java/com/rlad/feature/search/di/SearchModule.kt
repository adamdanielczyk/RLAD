package com.rlad.feature.search.di

import com.rlad.core.domain.navigation.Navigator
import com.rlad.feature.search.navigation.SearchNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface SearchModule {

    @Binds
    @IntoSet
    fun bindNavigator(impl: SearchNavigatorImpl): Navigator
}