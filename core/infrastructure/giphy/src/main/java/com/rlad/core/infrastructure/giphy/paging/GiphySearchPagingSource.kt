package com.rlad.core.infrastructure.giphy.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSourceFactory
import com.rlad.core.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.rlad.core.infrastructure.giphy.remote.ServerGif
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class GiphySearchPagingSourceFactory @Inject constructor(
    private val searchPagingSourceFactory: GiphySearchPagingSource.Factory,
) : CommonSearchPagingSourceFactory<ServerGif> {

    override fun create(query: String): PagingSource<Int, ServerGif> = searchPagingSourceFactory.create(query)
}

internal class GiphySearchPagingSource @AssistedInject constructor(
    private val remoteDataSource: GiphyRemoteDataSource,
    @Assisted private val query: String,
) : PagingSource<Int, ServerGif>() {

    @AssistedFactory
    interface Factory {
        fun create(query: String): GiphySearchPagingSource
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ServerGif> = try {
        val searchedGifs = remoteDataSource.searchGifs(
            offset = if (params is LoadParams.Append) params.key else remoteDataSource.getInitialPagingOffset(),
            limit = remoteDataSource.getPageSize(),
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

    override fun getRefreshKey(state: PagingState<Int, ServerGif>): Int = remoteDataSource.getInitialPagingOffset()
}