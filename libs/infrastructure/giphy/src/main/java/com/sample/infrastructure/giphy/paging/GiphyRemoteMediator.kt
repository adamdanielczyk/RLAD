package com.sample.infrastructure.giphy.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.sample.infrastructure.giphy.local.GifDataEntity
import com.sample.infrastructure.giphy.local.GiphyLocalDataSource
import com.sample.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.sample.infrastructure.giphy.remote.ServerGifData
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class GiphyRemoteMediator(
    private val localDataSource: GiphyLocalDataSource,
    private val remoteDataSource: GiphyRemoteDataSource,
) : RemoteMediator<Int, GifDataEntity>() {

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
            remoteDataSource.getGifsData(
                offset = pageSize * pageOffsetMultiplier,
                limit = pageSize,
            )
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