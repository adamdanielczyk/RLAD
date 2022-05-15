package com.rlad.infrastructure.giphy.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rlad.infrastructure.giphy.local.GifDataEntity
import com.rlad.infrastructure.giphy.local.GifDataEntity.OriginType
import com.rlad.infrastructure.giphy.local.GiphyLocalDataSource
import com.rlad.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.rlad.infrastructure.giphy.remote.ServerGifData
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
        fun create(query: String?): GiphyRemoteMediator
    }

    private var pageOffsetMultiplier = 0

    private val isSearchMode: Boolean = query != null

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GifDataEntity>,
    ): MediatorResult {
        if (loadType == LoadType.PREPEND) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        if (loadType == LoadType.REFRESH) {
            clearCachedDataOnRefresh()
        }

        val pageSize = state.config.pageSize

        val gifsData = try {
            val offset = pageSize * pageOffsetMultiplier
            getGifsData(offset, pageSize)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

        insertGifsData(gifsData)
        pageOffsetMultiplier++

        return MediatorResult.Success(endOfPaginationReached = gifsData.isEmpty())
    }

    private suspend fun clearCachedDataOnRefresh() {
        pageOffsetMultiplier = 0
        localDataSource.clearGifsData()
    }

    private suspend fun getGifsData(offset: Int, pageSize: Int): List<ServerGifData> = if (isSearchMode) {
        remoteDataSource.searchGifs(
            offset = offset,
            limit = pageSize,
            query = query!!,
        )
    } else {
        remoteDataSource.trendingGifs(
            offset = offset,
            limit = pageSize,
        )
    }

    private suspend fun insertGifsData(gifsData: List<ServerGifData>) {
        val newGifsData = gifsData.mapIndexed { index, serverGifData ->
            GifDataEntity(
                serverGifData = serverGifData,
                originType = if (isSearchMode) OriginType.Search else OriginType.Trending,
                orderId = index,
            )
        }
        localDataSource.insertGifsData(newGifsData)
    }
}