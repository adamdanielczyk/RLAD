package com.sample.infrastructure.rickandmorty.di

import com.sample.domain.model.DataSource
import com.sample.domain.model.DataSourceKey
import com.sample.domain.repository.ItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object RickAndMortyEntryPointModule {

    @Singleton
    @Provides
    fun provideEntryPoint(componentBuilder: RickAndMortyComponent.Builder): RickAndMortyEntryPoint =
        EntryPoints.get(componentBuilder.build(), RickAndMortyEntryPoint::class.java)

    @Provides
    @IntoMap
    @DataSourceKey(DataSource.RICK_AND_MORTY)
    fun provideItemsRepository(entryPoint: RickAndMortyEntryPoint): ItemsRepository = entryPoint.provideRickAndMortyRepository()
}