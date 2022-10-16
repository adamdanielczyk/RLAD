package com.rlad.core.infrastructure.giphy.di

import com.rlad.core.infrastructure.common.repository.ItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object GiphyEntryPointModule {

    @Singleton
    @Provides
    fun entryPoint(componentBuilder: GiphyComponent.Builder): GiphyEntryPoint =
        EntryPoints.get(componentBuilder.build(), GiphyEntryPoint::class.java)

    @Provides
    @IntoSet
    fun itemsRepository(entryPoint: GiphyEntryPoint): ItemsRepository = entryPoint.giphyRepository()
}