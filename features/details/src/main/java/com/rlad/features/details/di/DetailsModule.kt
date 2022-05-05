package com.rlad.features.details.di

import com.rlad.domain.navigation.DetailsNavigator
import com.rlad.domain.navigation.Navigator
import com.rlad.features.details.navigation.DetailsNavigatorImpl
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