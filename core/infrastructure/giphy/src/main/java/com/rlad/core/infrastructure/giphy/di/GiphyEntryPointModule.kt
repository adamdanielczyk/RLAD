package com.rlad.core.infrastructure.giphy.di

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
internal object GiphyEntryPointModule {

    @Singleton
    @Provides
    fun entryPoint(componentBuilder: GiphyComponent.Builder): GiphyEntryPoint =
        EntryPoints.get(componentBuilder.build(), GiphyEntryPoint::class.java)

    @Provides
    @IntoMap
    @DataSourceKey(DataSource.GIPHY)
    fun dataSourceConfiguration(entryPoint: GiphyEntryPoint): DataSourceConfiguration = entryPoint.dataSourceConfiguration()

    @Provides
    @IntoMap
    @DataSourceKey(DataSource.GIPHY)
    fun commonRepository(entryPoint: GiphyEntryPoint): CommonRepository = entryPoint.commonRepository()
}