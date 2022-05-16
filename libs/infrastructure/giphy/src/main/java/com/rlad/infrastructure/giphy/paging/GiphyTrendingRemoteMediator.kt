package com.rlad.infrastructure.giphy.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rlad.infrastructure.giphy.local.GifDataEntity
import com.rlad.infrastructure.giphy.local.GiphyLocalDataSource
import com.rlad.infrastructure.giphy.local.GiphyPreferencesLocalDataSource
import com.rlad.infrastructure.giphy.remote.GiphyRemoteDataSource
import com.rlad.infrastructure.giphy.remote.ServerGifData
import com.rlad.infrastructure.giphy.repository.GiphyRepository.Companion.PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class GiphyTrendingRemoteMediator @Inject constructor(
    private val localDataSource: GiphyLocalDataSource,
    private val remoteDataSource: GiphyRemoteDataSource,
    private val preferencesLocalDataSource: GiphyPreferencesLocalDataSource,
) : RemoteMediator<Int, GifDataEntity>() {

    override suspend fun initialize(): InitializeAction = if (shouldClearCache()) {
        InitializeAction.LAUNCH_INITIAL_REFRESH
    } else {
        InitializeAction.SKIP_INITIAL_REFRESH
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

        val serverGifs = try {
            remoteDataSource.trendingGifs(
                offset = getLastFetchedOffset(),
                limit = PAGE_SIZE,
            )
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

        val gifsData = serverGifs.data
        val pagination = serverGifs.pagination

        insertGifsData(gifsData)
        saveLastFetchedOffset(offset = pagination.offset + pagination.count)
        saveCurrentSyncTimestamp()

        return MediatorResult.Success(endOfPaginationReached = gifsData.isEmpty())
    }

    private suspend fun getLastFetchedOffset(): Int = preferencesLocalDataSource.getLastFetchedOffset() ?: 0

    private suspend fun saveLastFetchedOffset(offset: Int) {
        preferencesLocalDataSource.saveLastFetchedOffset(offset)
    }

    private suspend fun clearCachedDataOnRefresh() {
        saveLastFetchedOffset(offset = 0)
        localDataSource.clearGifsData()
    }

    private suspend fun insertGifsData(gifsData: List<ServerGifData>) {
        val newGifsData = gifsData.mapIndexed { index, serverGifData ->
            GifDataEntity(
                serverGifData = serverGifData,
                orderId = index,
            )
        }
        localDataSource.insertGifsData(newGifsData)
    }

    private suspend fun shouldClearCache(): Boolean {
        val lastSyncedTimestamp = preferencesLocalDataSource.getLastSyncedTimestamp() ?: return false
        val millisSinceLastSync = System.currentTimeMillis() - lastSyncedTimestamp
        val minutesSinceLastSync = TimeUnit.MILLISECONDS.toMinutes(millisSinceLastSync)
        return minutesSinceLastSync > CACHE_TIMEOUT_IN_MINUTES
    }

    private suspend fun saveCurrentSyncTimestamp() {
        preferencesLocalDataSource.saveLastSyncedTimestamp(System.currentTimeMillis())
    }

    private companion object {
        const val CACHE_TIMEOUT_IN_MINUTES = 10
    }
}