package com.sample.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterLocalDataSource
import com.sample.core.data.paging.CharacterRemoteMediator
import com.sample.core.data.remote.CharacterRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val localDataSource: CharacterLocalDataSource,
    private val remoteDataSource: CharacterRemoteDataSource,
) {

    fun getCharacterBy(id: Int): Flow<CharacterEntity> = localDataSource.getCharacterBy(id)

    fun getCharacters(nameOrLocation: String? = null): Flow<PagingData<CharacterEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                localDataSource = localDataSource,
                remoteDataSource = remoteDataSource,
                name = nameOrLocation
            ),
            pagingSourceFactory = { localDataSource.getCharactersBy(nameOrLocation.orEmpty()) }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}