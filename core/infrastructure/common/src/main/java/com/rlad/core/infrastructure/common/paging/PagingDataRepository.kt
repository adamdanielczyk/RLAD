package com.rlad.core.infrastructure.common.paging

import com.rlad.core.infrastructure.common.local.AppPreferencesLocalDataSource
import com.rlad.core.infrastructure.common.usecase.GetSelectedDataSourceUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

interface PagingDataRepository {
    suspend fun getLastSyncedTimestamp(): Long?
    suspend fun saveCurrentSyncTimestamp(timestamp: Long)
    suspend fun getNextOffsetToLoad(): Int?
    suspend fun saveNextOffsetToLoad(offset: Int)
}

internal class PagingDataRepositoryImpl @Inject constructor(
    private val appPreferencesLocalDataSource: AppPreferencesLocalDataSource,
    private val getSelectedDataSourceUseCase: GetSelectedDataSourceUseCase,
) : PagingDataRepository {

    override suspend fun getLastSyncedTimestamp(): Long? {
        val scopedKey = getScopedKey(key = LAST_SYNCED_TIMESTAMP_KEY)
        return appPreferencesLocalDataSource.get(scopedKey).firstOrNull()?.toLong()
    }

    override suspend fun saveCurrentSyncTimestamp(timestamp: Long) {
        val scopedKey = getScopedKey(key = LAST_SYNCED_TIMESTAMP_KEY)
        appPreferencesLocalDataSource.save(
            key = scopedKey,
            value = timestamp.toString(),
        )
    }

    override suspend fun getNextOffsetToLoad(): Int? {
        val scopedKey = getScopedKey(key = NEXT_OFFSET_TO_LOAD)
        return appPreferencesLocalDataSource.get(scopedKey).firstOrNull()?.toInt()
    }

    override suspend fun saveNextOffsetToLoad(offset: Int) {
        val scopedKey = getScopedKey(key = NEXT_OFFSET_TO_LOAD)
        appPreferencesLocalDataSource.save(
            key = scopedKey,
            value = offset.toString(),
        )
    }

    private suspend fun getScopedKey(key: String): String {
        val selectedDataSourceName = getSelectedDataSourceUseCase().first().dataSourceName
        return "${selectedDataSourceName}_$key"
    }

    private companion object {
        const val NEXT_OFFSET_TO_LOAD = "NEXT_OFFSET_TO_LOAD"
        const val LAST_SYNCED_TIMESTAMP_KEY = "LAST_SYNCED_TIMESTAMP_KEY"
    }
}