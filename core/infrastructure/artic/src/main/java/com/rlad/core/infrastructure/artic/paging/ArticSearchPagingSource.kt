package com.rlad.core.infrastructure.artic.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rlad.core.infrastructure.artic.remote.ArticRemoteDataSource
import com.rlad.core.infrastructure.artic.remote.ServerArtwork
import com.rlad.core.infrastructure.common.paging.CommonSearchPagingSourceFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class ArticSearchPagingSourceFactory @Inject constructor(
    private val searchPagingSourceFactory: ArticSearchPagingSource.Factory,
) : CommonSearchPagingSourceFactory<ServerArtwork> {

    override fun create(query: String): PagingSource<Int, ServerArtwork> = searchPagingSourceFactory.create(query)
}

internal class ArticSearchPagingSource @AssistedInject constructor(
    private val remoteDataSource: ArticRemoteDataSource,
    @Assisted private val query: String,
) : PagingSource<Int, ServerArtwork>() {

    @AssistedFactory
    interface Factory {
        fun create(query: String): ArticSearchPagingSource
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ServerArtwork> = try {
        val searchedArtworks = remoteDataSource.searchArtworks(
            from = if (params is LoadParams.Append) params.key else remoteDataSource.getInitialPagingOffset(),
            size = remoteDataSource.getPageSize(),
            query = query,
        )

        val pagination = searchedArtworks.pagination
        val data = searchedArtworks.data
        LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = if (data.isNotEmpty()) pagination.offset + pagination.limit else null,
        )
    } catch (exception: IOException) {
        LoadResult.Error(exception)
    } catch (exception: HttpException) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, ServerArtwork>): Int = remoteDataSource.getInitialPagingOffset()
}