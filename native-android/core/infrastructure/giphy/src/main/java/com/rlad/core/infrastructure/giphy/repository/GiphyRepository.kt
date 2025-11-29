package com.rlad.core.infrastructure.giphy.repository

import com.rlad.core.domain.model.DataSource
import com.rlad.core.domain.model.DataSourceKey
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.paging.CommonRemoteMediator
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSource
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.CommonRepositoryImpl
import com.rlad.core.infrastructure.giphy.local.GifEntity
import com.rlad.core.infrastructure.giphy.remote.ServerGif
import com.rlad.core.infrastructure.giphy.remote.ServerGifsRoot
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject

@Inject
@ContributesIntoMap(AppScope::class)
@DataSourceKey(DataSource.GIPHY)
class GiphyRepository(
    private val localDataSource: CommonLocalDataSource<GifEntity>,
    private val remoteDataSource: CommonRemoteDataSource<ServerGifsRoot, ServerGif>,
    private val remoteMediator: CommonRemoteMediator<GifEntity, ServerGif, ServerGifsRoot>,
    private val searchPagingSourceFactory: CommonSearchPagingSource.Factory<ServerGif, ServerGifsRoot>,
    private val modelMapper: ModelMapper<GifEntity, ServerGif>,
) : CommonRepository by CommonRepositoryImpl(localDataSource, remoteDataSource, remoteMediator, searchPagingSourceFactory, modelMapper)
