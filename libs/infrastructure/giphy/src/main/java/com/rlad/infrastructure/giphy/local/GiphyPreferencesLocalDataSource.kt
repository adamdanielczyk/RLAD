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

    suspend fun getNextOffsetToLoad(): Int? = appPreferencesLocalDataSource.get(NEXT_OFFSET_TO_LOAD).firstOrNull()?.toInt()

    suspend fun saveNextOffsetToLoad(offset: Int) {
        appPreferencesLocalDataSource.save(
            key = NEXT_OFFSET_TO_LOAD,
            value = offset.toString()
        )
    }

    private companion object {
        const val LAST_SYNCED_TIMESTAMP_KEY = "GIPHY_LAST_SYNCED_TIMESTAMP_KEY"
        const val NEXT_OFFSET_TO_LOAD = "GIPHY_NEXT_OFFSET_TO_LOAD"
    }
}