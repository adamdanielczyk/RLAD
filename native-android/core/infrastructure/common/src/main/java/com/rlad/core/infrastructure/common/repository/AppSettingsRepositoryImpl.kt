package com.rlad.core.infrastructure.common.repository

import com.rlad.core.domain.repository.AppSettingsRepository
import com.rlad.core.infrastructure.common.local.AppPreferencesLocalDataSource
import com.rlad.core.domain.model.DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class AppSettingsRepositoryImpl(
    private val appPreferencesLocalDataSource: AppPreferencesLocalDataSource,
) : AppSettingsRepository {

    override fun getSelectedDataSource(): Flow<DataSource?> = appPreferencesLocalDataSource.get(SELECTED_DATA_SOURCE_KEY).map { dataSourceName ->
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
