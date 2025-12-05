package com.rlad.core.infrastructure.common.paging

import com.rlad.core.infrastructure.common.local.AppPreferencesLocalDataSource
import com.rlad.core.infrastructure.common.usecase.GetSelectedDataSourceUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

interface PagingDataRepository {
    suspend fun getLastSyncedTimestamp(): Long?
    suspend fun saveCurrentSyncTimestamp(timestamp: Long)
    suspend fun getNextOffsetToLoad(): Int?
    suspend fun saveNextOffsetToLoad(offset: Int)
}

@Inject
@ContributesBinding(AppScope::class)
class PagingDataRepositoryImpl(
    private val appPreferencesLocalDataSource: AppPreferencesLocalDataSource,
    private val getSelectedDataSourceUseCase: GetSelectedDataSourceUseCase,
) : PagingDataRepository {

    override suspend fun getLastSyncedTimestamp(): Long? = get(LAST_SYNCED_TIMESTAMP_KEY)?.toLong()

    override suspend fun saveCurrentSyncTimestamp(timestamp: Long) {
        save(key = LAST_SYNCED_TIMESTAMP_KEY, value = timestamp.toString())
    }

    override suspend fun getNextOffsetToLoad(): Int? = get(NEXT_OFFSET_TO_LOAD)?.toInt()

    override suspend fun saveNextOffsetToLoad(offset: Int) {
        save(key = NEXT_OFFSET_TO_LOAD, value = offset.toString())
    }

    private suspend fun get(key: String): String? {
        val scopedKey = getScopedKey(key)
        return appPreferencesLocalDataSource.get(scopedKey).firstOrNull()
    }

    private suspend fun save(key: String, value: String) {
        val scopedKey = getScopedKey(key)
        appPreferencesLocalDataSource.save(
            key = scopedKey,
            value = value,
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
