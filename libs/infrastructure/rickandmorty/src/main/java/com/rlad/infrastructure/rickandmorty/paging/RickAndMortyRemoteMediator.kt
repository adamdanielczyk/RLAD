package com.rlad.infrastructure.rickandmorty.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rlad.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.rlad.infrastructure.rickandmorty.remote.RickAndMortyRemoteDataSource
import com.rlad.infrastructure.rickandmorty.remote.ServerCharacter
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class RickAndMortyRemoteMediator @Inject constructor(
    private val localDataSource: RickAndMortyLocalDataSource,
    private val remoteDataSource: RickAndMortyRemoteDataSource,
) : RemoteMediator<Int, CharacterEntity>() {

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
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        if (loadType == LoadType.REFRESH) {
            clearCachedDataOnRefresh()
        }

        val characters = try {
            remoteDataSource.getCharacters(
                page = nextKey,
                name = null,
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

    private suspend fun clearCachedDataOnRefresh() {
        nextKey = 1
        localDataSource.clearCharacters()
    }

    private suspend fun insertCharacters(serverCharacters: List<ServerCharacter>) {
        val newLocalCharacters = serverCharacters.map(::CharacterEntity)
        localDataSource.insertCharacters(newLocalCharacters)
    }
}