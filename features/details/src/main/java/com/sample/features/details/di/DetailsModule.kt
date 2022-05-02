package com.sample.features.details.di

import com.sample.domain.navigation.DetailsNavigator
import com.sample.features.details.navigation.DetailsNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DetailsModule {

    @Binds
    fun provideDetailsNavigator(impl: DetailsNavigatorImpl): DetailsNavigator
}