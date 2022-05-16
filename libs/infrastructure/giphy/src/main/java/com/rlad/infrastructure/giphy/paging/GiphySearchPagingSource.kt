package com.rlad.infrastructure.giphy.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rlad.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.rlad.infrastructure.giphy.remote.ServerGifData
import com.rlad.infrastructure.giphy.repository.GiphyRepository.Companion.INITIAL_PAGING_OFFSET
import com.rlad.infrastructure.giphy.repository.GiphyRepository.Companion.PAGE_SIZE
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

internal class GiphySearchPagingSource @AssistedInject constructor(
    private val remoteDataSource: GiphyRemoteDataSource,
    @Assisted private val query: String,
) : PagingSource<Int, ServerGifData>() {

    @AssistedFactory
    interface Factory {
        fun create(query: String): GiphySearchPagingSource
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ServerGifData> = try {
        val searchedGifs = remoteDataSource.searchGifs(
            offset = if (params is LoadParams.Append) params.key else INITIAL_PAGING_OFFSET,
            limit = PAGE_SIZE,
            query = query,
        )

        val pagination = searchedGifs.pagination
        LoadResult.Page(
            data = searchedGifs.data,
            prevKey = null,
            nextKey = pagination.offset + pagination.count
        )
    } catch (exception: IOException) {
        LoadResult.Error(exception)
    } catch (exception: HttpException) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, ServerGifData>): Int = INITIAL_PAGING_OFFSET
}