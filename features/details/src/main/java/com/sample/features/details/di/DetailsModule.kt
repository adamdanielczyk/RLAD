package com.sample.features.details.di

import com.sample.domain.navigation.DetailsNavigator
import com.sample.domain.navigation.Navigator
import com.sample.features.details.navigation.DetailsNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface DetailsModule {

    @Binds
    fun provideDetailsNavigator(impl: DetailsNavigatorImpl): DetailsNavigator

    @Binds
    @IntoSet
    fun provideNavigator(impl: DetailsNavigatorImpl): Navigator
}