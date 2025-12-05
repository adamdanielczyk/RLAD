package com.rlad.core.infrastructure.artic.repository

import com.rlad.core.domain.model.DataSource
import com.rlad.core.domain.model.DataSourceKey
import com.rlad.core.infrastructure.artic.local.ArtworkEntity
import com.rlad.core.infrastructure.artic.remote.ServerArtwork
import com.rlad.core.infrastructure.artic.remote.ServerArtworksRoot
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.paging.CommonRemoteMediator
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSource
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.CommonRepositoryImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject

@Inject
@ContributesIntoMap(AppScope::class)
@DataSourceKey(DataSource.ARTIC)
class ArticRepository(
    private val localDataSource: CommonLocalDataSource<ArtworkEntity>,
    private val remoteDataSource: CommonRemoteDataSource<ServerArtworksRoot, ServerArtwork>,
    private val remoteMediator: CommonRemoteMediator<ArtworkEntity, ServerArtwork, ServerArtworksRoot>,
    private val searchPagingSourceFactory: CommonSearchPagingSource.Factory<ServerArtwork, ServerArtworksRoot>,
    private val modelMapper: ModelMapper<ArtworkEntity, ServerArtwork>,
) : CommonRepository by CommonRepositoryImpl(localDataSource, remoteDataSource, remoteMediator, searchPagingSourceFactory, modelMapper)
