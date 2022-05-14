package com.rlad.infrastructure.giphy.initializer

import com.rlad.domain.initializer.AppInitializer
import com.rlad.infrastructure.giphy.local.GiphyLocalDataSource
import com.rlad.infrastructure.giphy.repository.GiphyRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class ClearTrendingGifsDataAppInitializer @Inject constructor(
    private val localDataSource: GiphyLocalDataSource,
    private val giphyRepository: GiphyRepository,
) : AppInitializer {

    override suspend fun initialize() {
        val lastSyncedTimestamp = giphyRepository.getLastSyncedTimestamp() ?: return
        val currentTimeMillis = System.currentTimeMillis()
        val millisSinceLastSync = currentTimeMillis - lastSyncedTimestamp
        val minutesSinceLastSync = TimeUnit.MILLISECONDS.toMinutes(millisSinceLastSync)

        if (minutesSinceLastSync > INVALIDATION_PERIOD_IN_MINUTES) {
            localDataSource.deleteTrendingGifs()
        }
    }

    private companion object {
        const val INVALIDATION_PERIOD_IN_MINUTES = 10
    }
}