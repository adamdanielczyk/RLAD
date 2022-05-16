package com.rlad.infrastructure.giphy.local

import com.rlad.infrastructure.common.local.AppPreferencesLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class GiphyPreferencesLocalDataSource @Inject constructor(
    private val appPreferencesLocalDataSource: AppPreferencesLocalDataSource,
) {

    suspend fun getLastSyncedTimestamp(): Long? = appPreferencesLocalDataSource.get(LAST_SYNCED_TIMESTAMP_KEY).firstOrNull()?.toLong()

    suspend fun saveLastSyncedTimestamp(timestamp: Long) {
        appPreferencesLocalDataSource.save(
            key = LAST_SYNCED_TIMESTAMP_KEY,
            value = timestamp.toString()
        )
    }

    suspend fun getLastFetchedOffset(): Int? = appPreferencesLocalDataSource.get(LAST_FETCHED_OFFSET).firstOrNull()?.toInt()

    suspend fun saveLastFetchedOffset(offset: Int) {
        appPreferencesLocalDataSource.save(
            key = LAST_FETCHED_OFFSET,
            value = offset.toString()
        )
    }

    private companion object {
        const val LAST_SYNCED_TIMESTAMP_KEY = "GIPHY_LAST_SYNCED_TIMESTAMP_KEY"
        const val LAST_FETCHED_OFFSET = "GIPHY_LAST_FETCHED_OFFSET"
    }
}