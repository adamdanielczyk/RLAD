package com.rlad.core.infrastructure.common.repository

import com.rlad.core.domain.repository.AppSettingsRepository
import com.rlad.core.infrastructure.common.local.AppPreferencesLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AppSettingsRepositoryImpl @Inject constructor(
    private val appPreferencesLocalDataSource: AppPreferencesLocalDataSource,
) : AppSettingsRepository {

    override fun getSelectedDataSourceName(): Flow<String?> = appPreferencesLocalDataSource.get(SELECTED_DATA_SOURCE_KEY)

    override suspend fun saveSelectedDataSourceName(dataSourceName: String) {
        appPreferencesLocalDataSource.save(
            key = SELECTED_DATA_SOURCE_KEY,
            value = dataSourceName
        )
    }

    private companion object {
        const val SELECTED_DATA_SOURCE_KEY = "SELECTED_DATA_SOURCE_KEY"
    }
}