package com.rlad.core.infrastructure.rickandmorty.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rlad.core.infrastructure.rickandmorty.remote.RickAndMortyRemoteDataSource
import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter
import com.rlad.core.infrastructure.rickandmorty.repository.RickAndMortyRepository.Companion.INITIAL_PAGING_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

internal class RickAndMortySearchPagingSource @AssistedInject constructor(
    private val remoteDataSource: RickAndMortyRemoteDataSource,
    @Assisted private val query: String,
) : PagingSource<Int, ServerCharacter>() {

    @AssistedFactory
    interface Factory {
        fun create(query: String): RickAndMortySearchPagingSource
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ServerCharacter> = try {
        val nextKey = if (params is LoadParams.Append) params.key else INITIAL_PAGING_KEY
        val characters = remoteDataSource.getCharacters(
            page = nextKey,
            name = query
        )

        LoadResult.Page(
            data = characters,
            prevKey = null,
            nextKey = nextKey + 1
        )
    } catch (exception: IOException) {
        LoadResult.Error(exception)
    } catch (exception: HttpException) {
        if (exception.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        } else {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ServerCharacter>): Int = INITIAL_PAGING_KEY
}