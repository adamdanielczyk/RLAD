package com.sample.infrastructure.giphy.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.sample.infrastructure.giphy.local.GifDataEntity
import com.sample.infrastructure.giphy.local.GiphyLocalDataSource
import com.sample.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.sample.infrastructure.giphy.remote.ServerGifData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class GiphyRemoteMediator @AssistedInject constructor(
    private val localDataSource: GiphyLocalDataSource,
    private val remoteDataSource: GiphyRemoteDataSource,
    @Assisted private val query: String?,
) : RemoteMediator<Int, GifDataEntity>() {

    @AssistedFactory
    interface Factory {
        fun create(query: String? = null): GiphyRemoteMediator
    }

    private var pageOffsetMultiplier = 0

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GifDataEntity>,
    ): MediatorResult {
        if (loadType == LoadType.PREPEND) {
            // Paging is not supported in two directions currently, return early in case of prepend load type
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        val pageSize = state.config.pageSize

        val gifsData = try {
            val offset = pageSize * pageOffsetMultiplier
            if (query != null) {
                remoteDataSource.searchGifs(
                    offset = offset,
                    limit = pageSize,
                    query = query,
                )
            } else {
                remoteDataSource.trendingGifs(
                    offset = offset,
                    limit = pageSize,
                )
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

        insertGifsData(gifsData)
        pageOffsetMultiplier++

        return MediatorResult.Success(endOfPaginationReached = gifsData.isEmpty())
    }

    private suspend fun insertGifsData(gifsData: List<ServerGifData>) {
        val newGifsData = gifsData.map(::GifDataEntity)
        localDataSource.insertGifsData(newGifsData)
    }
}