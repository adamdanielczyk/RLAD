package com.rlad.core.infrastructure.rickandmorty.repository

import com.rlad.core.domain.model.DataSource
import com.rlad.core.domain.model.DataSourceKey
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.paging.CommonRemoteMediator
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSource
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.CommonRepositoryImpl
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter
import com.rlad.core.infrastructure.rickandmorty.remote.ServerGetCharacters
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap

@ContributesIntoMap(AppScope::class)
@DataSourceKey(DataSource.RICKANDMORTY)
class RickAndMortyRepository(
    private val localDataSource: CommonLocalDataSource<CharacterEntity>,
    private val remoteDataSource: CommonRemoteDataSource<ServerGetCharacters, ServerCharacter>,
    private val remoteMediator: CommonRemoteMediator<CharacterEntity, ServerCharacter, ServerGetCharacters>,
    private val searchPagingSourceFactory: CommonSearchPagingSource.Factory<ServerCharacter, ServerGetCharacters>,
    private val modelMapper: ModelMapper<CharacterEntity, ServerCharacter>,
) : CommonRepository by CommonRepositoryImpl(localDataSource, remoteDataSource, remoteMediator, searchPagingSourceFactory, modelMapper)
