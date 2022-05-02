package com.sample.features.search.di

import com.sample.domain.navigation.SearchNavigator
import com.sample.features.search.navigation.SearchNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface SearchModule {

    @Binds
    fun provide(impl: SearchNavigatorImpl): SearchNavigator
}