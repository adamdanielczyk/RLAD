package com.rlad.core.infrastructure.common.paging

import android.app.Application
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class CommonRemoteMediator<LocalModel : Any, RemoteModel : Any, RootRemoteData : Any> @Inject constructor(
    private val localDataSource: CommonLocalDataSource<LocalModel>,
    private val remoteDataSource: CommonRemoteDataSource<RootRemoteData, RemoteModel>,
    private val modelMapper: ModelMapper<LocalModel, RemoteModel>,
    private val pagingDataRepository: PagingDataRepository,
    private val application: Application,
) : RemoteMediator<Int, LocalModel>() {

    override suspend fun initialize(): InitializeAction = if (shouldClearCache()) {
        InitializeAction.LAUNCH_INITIAL_REFRESH
    } else {
        InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalModel>,
    ): MediatorResult {
        if (loadType == LoadType.PREPEND) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        if (loadType == LoadType.REFRESH) {
            clearCachedDataOnRefresh()
        }

        val nextOffsetToLoad = getNextOffsetToLoad()
        val rootData = try {
            remoteDataSource.getRootData(offset = nextOffsetToLoad)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

        val items = remoteDataSource.getItems(rootData)

        insertData(items)
        saveNextOffsetToLoad(
            offset = remoteDataSource.getNextPagingOffset(
                rootData = rootData,
                currentlyLoadedOffset = nextOffsetToLoad
            )
        )
        saveCurrentSyncTimestamp()

        return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
    }

    private suspend fun clearCachedDataOnRefresh() {
        saveNextOffsetToLoad(offset = remoteDataSource.getInitialPagingOffset())
        localDataSource.clear()
        application.cacheDir.deleteRecursively()
    }

    private suspend fun insertData(remoteData: List<RemoteModel>) {
        val localData = remoteData.map(modelMapper::toLocalModel)
        localDataSource.insert(localData)
    }

    private suspend fun shouldClearCache(): Boolean {
        val lastSyncedTimestamp = getLastSyncedTimestamp() ?: return false
        val millisSinceLastSync = System.currentTimeMillis() - lastSyncedTimestamp
        val minutesSinceLastSync = millisSinceLastSync.milliseconds.inWholeMinutes
        return minutesSinceLastSync > CACHE_TIMEOUT_IN_MINUTES
    }

    private suspend fun getNextOffsetToLoad(): Int = pagingDataRepository.getNextOffsetToLoad() ?: remoteDataSource.getInitialPagingOffset()

    private suspend fun saveNextOffsetToLoad(offset: Int) {
        pagingDataRepository.saveNextOffsetToLoad(offset)
    }

    private suspend fun getLastSyncedTimestamp(): Long? = pagingDataRepository.getLastSyncedTimestamp()

    private suspend fun saveCurrentSyncTimestamp() {
        pagingDataRepository.saveCurrentSyncTimestamp(System.currentTimeMillis())
    }

    private companion object {
        const val CACHE_TIMEOUT_IN_MINUTES = 30
    }
}