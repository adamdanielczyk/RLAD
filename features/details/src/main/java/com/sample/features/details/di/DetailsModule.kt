package com.sample.features.details.di

import com.sample.core.navigation.DetailsNavigator
import com.sample.features.details.navigation.DetailsNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DetailsModule {

    @Binds
    fun provide(impl: DetailsNavigatorImpl): DetailsNavigator
}