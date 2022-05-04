package com.sample.infrastructure.rickandmorty.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.sample.infrastructure.rickandmorty.local.CharacterEntity
import com.sample.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.sample.infrastructure.rickandmorty.remote.RickAndMortyRemoteDataSource
import com.sample.infrastructure.rickandmorty.remote.ServerCharacter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class RickAndMortyRemoteMediator @AssistedInject constructor(
    private val localDataSource: RickAndMortyLocalDataSource,
    private val remoteDataSource: RickAndMortyRemoteDataSource,
    @Assisted private val query: String?,
) : RemoteMediator<Int, CharacterEntity>() {

    @AssistedFactory
    interface Factory {
        fun create(query: String? = null): RickAndMortyRemoteMediator
    }

    /**
     * Store page key in memory.
     * For better experience, last loaded page key could be stored on disk.
     */
    private var nextKey = 1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult {
        if (loadType == LoadType.PREPEND) {
            // Paging is not supported in two directions currently, return early in case of prepend load type
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        val characters = try {
            remoteDataSource.getCharacters(
                page = nextKey,
                name = query
            )
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

        insertCharacters(characters)
        nextKey++

        return MediatorResult.Success(endOfPaginationReached = characters.isEmpty())
    }

    private suspend fun insertCharacters(serverCharacters: List<ServerCharacter>) {
        val newLocalCharacters = serverCharacters.map(::CharacterEntity)
        localDataSource.insertCharacters(newLocalCharacters)
    }
}