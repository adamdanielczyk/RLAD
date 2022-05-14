package com.rlad.infrastructure.rickandmorty.di

import com.rlad.infrastructure.common.repository.ItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object RickAndMortyEntryPointModule {

    @Singleton
    @Provides
    fun entryPoint(componentBuilder: RickAndMortyComponent.Builder): RickAndMortyEntryPoint =
        EntryPoints.get(componentBuilder.build(), RickAndMortyEntryPoint::class.java)

    @Provides
    @IntoSet
    fun itemsRepository(entryPoint: RickAndMortyEntryPoint): ItemsRepository = entryPoint.rickAndMortyRepository()
}