package com.rlad.core.infrastructure.common.repository

import com.rlad.core.domain.repository.AppSettingsRepository
import com.rlad.core.infrastructure.common.local.AppPreferencesLocalDataSource
import com.rlad.core.infrastructure.common.model.DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class AppSettingsRepositoryImpl @Inject constructor(
    private val appPreferencesLocalDataSource: AppPreferencesLocalDataSource,
) : AppSettingsRepository {

    fun getSelectedDataSource(): Flow<DataSource?> = appPreferencesLocalDataSource.get(SELECTED_DATA_SOURCE_KEY).map { dataSourceName ->
        dataSourceName?.let { DataSource.fromString(it) }
    }

    override suspend fun saveSelectedDataSourceName(dataSourceName: String) {
        appPreferencesLocalDataSource.save(
            key = SELECTED_DATA_SOURCE_KEY,
            value = dataSourceName,
        )
    }

    private companion object {
        const val SELECTED_DATA_SOURCE_KEY = "SELECTED_DATA_SOURCE_KEY"
    }
}
