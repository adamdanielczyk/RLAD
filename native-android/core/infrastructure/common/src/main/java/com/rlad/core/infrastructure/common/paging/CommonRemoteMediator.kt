package com.rlad.core.infrastructure.common.paging

import android.app.Application
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
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

        saveCurrentSyncTimestamp()

        return try {
            val config = state.config
            loadItems(
                loadType = loadType,
                totalItemsToLoad = if (loadType == LoadType.REFRESH) config.initialLoadSize else config.pageSize,
                loadedItemsCount = 0,
            )
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: ClientRequestException) {
            MediatorResult.Error(exception)
        } catch (exception: ServerResponseException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun clearCachedDataOnRefresh() {
        saveNextOffsetToLoad(offset = remoteDataSource.getInitialPagingOffset())
        localDataSource.clear()
        application.cacheDir.deleteRecursively()
    }

    private suspend fun loadItems(loadType: LoadType, totalItemsToLoad: Int, loadedItemsCount: Int): MediatorResult {
        val nextOffsetToLoad = getNextOffsetToLoad()

        val rootData = remoteDataSource.getRootData(offset = nextOffsetToLoad, pageSize = totalItemsToLoad - loadedItemsCount)
        val items = remoteDataSource.getItems(rootData)

        insertData(items)
        saveNextOffsetToLoad(
            offset = remoteDataSource.getNextPagingOffset(
                rootData = rootData,
                currentlyLoadedPage = nextOffsetToLoad,
            ),
        )

        val totalLoadedItemsCount = loadedItemsCount + items.size

        return when {
            items.isEmpty() -> MediatorResult.Success(endOfPaginationReached = true)
            totalLoadedItemsCount >= totalItemsToLoad -> MediatorResult.Success(endOfPaginationReached = false)
            else -> loadItems(loadType = loadType, totalItemsToLoad = totalItemsToLoad, loadedItemsCount = totalLoadedItemsCount)
        }
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
