package com.rlad.core.infrastructure.giphy.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSourceFactory
import com.rlad.core.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.rlad.core.infrastructure.giphy.remote.GiphyRemoteDataSource.Companion.INITIAL_PAGING_OFFSET
import com.rlad.core.infrastructure.giphy.remote.GiphyRemoteDataSource.Companion.PAGE_SIZE
import com.rlad.core.infrastructure.giphy.remote.ServerGifData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class GiphySearchPagingSourceFactory @Inject constructor(
    private val searchPagingSourceFactory: GiphySearchPagingSource.Factory,
) : CommonSearchPagingSourceFactory<ServerGifData> {

    override fun create(query: String): PagingSource<Int, ServerGifData> = searchPagingSourceFactory.create(query)
}

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
        val data = searchedGifs.data
        LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = if (data.isNotEmpty()) pagination.offset + pagination.count else null,
        )
    } catch (exception: IOException) {
        LoadResult.Error(exception)
    } catch (exception: HttpException) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, ServerGifData>): Int = INITIAL_PAGING_OFFSET
}