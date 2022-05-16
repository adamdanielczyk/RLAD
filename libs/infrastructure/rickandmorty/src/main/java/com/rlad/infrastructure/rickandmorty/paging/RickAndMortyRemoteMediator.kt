package com.rlad.infrastructure.rickandmorty.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rlad.infrastructure.rickandmorty.local.CharacterEntity
import com.rlad.infrastructure.rickandmorty.local.RickAndMortyLocalDataSource
import com.rlad.infrastructure.rickandmorty.local.RickAndMortyPreferencesLocalDataSource
import com.rlad.infrastructure.rickandmorty.remote.RickAndMortyRemoteDataSource
import com.rlad.infrastructure.rickandmorty.remote.ServerCharacter
import com.rlad.infrastructure.rickandmorty.repository.RickAndMortyRepository.Companion.INITIAL_PAGING_KEY
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class RickAndMortyRemoteMediator @Inject constructor(
    private val localDataSource: RickAndMortyLocalDataSource,
    private val remoteDataSource: RickAndMortyRemoteDataSource,
    private val preferencesLocalDataSource: RickAndMortyPreferencesLocalDataSource,
) : RemoteMediator<Int, CharacterEntity>() {

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

        val nextPageToLoad = getNextPageToLoad()
        val characters = try {
            remoteDataSource.getCharacters(
                page = nextPageToLoad,
                name = null,
            )
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

        insertCharacters(characters)
        saveNextPageToLoad(page = nextPageToLoad + 1)

        return MediatorResult.Success(endOfPaginationReached = characters.isEmpty())
    }

    private suspend fun clearCachedDataOnRefresh() {
        saveNextPageToLoad(INITIAL_PAGING_KEY)
        localDataSource.clearCharacters()
    }

    private suspend fun insertCharacters(serverCharacters: List<ServerCharacter>) {
        val newLocalCharacters = serverCharacters.map(::CharacterEntity)
        localDataSource.insertCharacters(newLocalCharacters)
    }

    private suspend fun getNextPageToLoad(): Int = preferencesLocalDataSource.getNextPageToLoad() ?: INITIAL_PAGING_KEY

    private suspend fun saveNextPageToLoad(page: Int) {
        preferencesLocalDataSource.saveNextPageToLoad(page)
    }
}