package com.sample.infrastructure.giphy.di

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
internal object GiphyEntryPointModule {

    @Singleton
    @Provides
    fun provideEntryPoint(componentBuilder: GiphyComponent.Builder): GiphyEntryPoint =
        EntryPoints.get(componentBuilder.build(), GiphyEntryPoint::class.java)

    @Provides
    @IntoMap
    @DataSourceKey(DataSource.GIPHY)
    fun provideItemsRepository(entryPoint: GiphyEntryPoint): ItemsRepository = entryPoint.provideGiphyRepository()
}