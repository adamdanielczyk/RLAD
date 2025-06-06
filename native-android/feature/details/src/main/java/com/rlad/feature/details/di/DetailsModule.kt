package com.rlad.feature.details.di

import com.rlad.core.domain.navigation.DetailsNavigator
import com.rlad.core.domain.navigation.Navigator
import com.rlad.feature.details.navigation.DetailsNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface DetailsModule {

    @Binds
    fun bindDetailsNavigator(impl: DetailsNavigatorImpl): DetailsNavigator

    @Binds
    @IntoSet
    fun bindNavigator(impl: DetailsNavigatorImpl): Navigator
}