package com.rlad.core.infrastructure.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import java.io.IOException

class CommonSearchPagingSource<RemoteModel : Any, RootRemoteData : Any> @AssistedInject constructor(
    private val remoteDataSource: CommonRemoteDataSource<RootRemoteData, RemoteModel>,
    @Assisted private val query: String,
) : PagingSource<Int, RemoteModel>() {

    @AssistedFactory
    interface Factory<RemoteModel : Any, RootRemoteData : Any> {
        fun create(query: String): CommonSearchPagingSource<RemoteModel, RootRemoteData>
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RemoteModel> = try {
        val nextKey = if (params is LoadParams.Append) params.key else remoteDataSource.getInitialPagingOffset()
        val rootSearchData = remoteDataSource.search(
            query = query,
            offset = nextKey,
            pageSize = params.loadSize,
        )

        val searchData = remoteDataSource.getItems(rootSearchData)
        LoadResult.Page(
            data = searchData,
            prevKey = null,
            nextKey = if (searchData.isNotEmpty()) remoteDataSource.getNextPagingOffset(rootSearchData, nextKey) else null,
        )
    } catch (exception: IOException) {
        LoadResult.Error(exception)
    } catch (exception: ClientRequestException) {
        if (exception.response.status == HttpStatusCode.NotFound) {
            LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null,
            )
        } else {
            LoadResult.Error(exception)
        }
    } catch (exception: ServerResponseException) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, RemoteModel>): Int = remoteDataSource.getInitialPagingOffset()
}
