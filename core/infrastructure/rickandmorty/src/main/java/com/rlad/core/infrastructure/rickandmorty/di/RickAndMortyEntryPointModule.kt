package com.rlad.core.infrastructure.rickandmorty.di

import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.common.model.DataSourceKey
import com.rlad.core.infrastructure.common.repository.CommonRepository
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
    fun entryPoint(componentBuilder: RickAndMortyComponent.Builder): RickAndMortyEntryPoint =
        EntryPoints.get(componentBuilder.build(), RickAndMortyEntryPoint::class.java)

    @Provides
    @IntoMap
    @DataSourceKey(DataSource.RICKANDMORTY)
    fun dataSourceConfiguration(entryPoint: RickAndMortyEntryPoint): DataSourceConfiguration = entryPoint.dataSourceConfiguration()

    @Provides
    @IntoMap
    @DataSourceKey(DataSource.RICKANDMORTY)
    fun commonRepository(entryPoint: RickAndMortyEntryPoint): CommonRepository = entryPoint.commonRepository()
}