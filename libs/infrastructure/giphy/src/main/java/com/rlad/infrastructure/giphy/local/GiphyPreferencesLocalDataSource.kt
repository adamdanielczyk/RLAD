package com.rlad.infrastructure.giphy.local

import javax.inject.Inject

internal class GiphyPreferencesLocalDataSource @Inject constructor(
    private val preferencesDao: GiphyPreferencesDao,
) {

    suspend fun getLastSyncedTimestamp(): Long? = preferencesDao.getByKey(LAST_SYNCED_TIMESTAMP_KEY)?.value?.toLong()

    suspend fun saveLastSyncedTimestamp(timestamp: Long) {
        preferencesDao.insert(GiphyPreferencesEntity(
            key = LAST_SYNCED_TIMESTAMP_KEY,
            value = timestamp.toString()
        ))
    }

    suspend fun getLastFetchedOffset(): Int? = preferencesDao.getByKey(LAST_FETCHED_OFFSET)?.value?.toInt()

    suspend fun saveLastFetchedOffset(offset: Int) {
        preferencesDao.insert(GiphyPreferencesEntity(
            key = LAST_FETCHED_OFFSET,
            value = offset.toString()
        ))
    }

    private companion object {
        const val LAST_SYNCED_TIMESTAMP_KEY = "LAST_SYNCED_TIMESTAMP_KEY"
        const val LAST_FETCHED_OFFSET = "LAST_FETCHED_OFFSET"
    }
}