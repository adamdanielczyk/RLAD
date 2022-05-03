package com.sample.infrastructure.common.repository

import android.content.SharedPreferences
import com.sample.domain.model.DataSource
import com.sample.domain.repository.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AppSettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : AppSettingsRepository {

    override suspend fun getSelectedDataSource(): DataSource = withContext(Dispatchers.IO) {
        val sourceName = sharedPreferences.getString(SELECTED_DATA_SOURCE_KEY, DataSource.GIPHY.sourceName)
        DataSource.values().first { it.sourceName == sourceName }
    }

    override suspend fun saveSelectedDataSource(dataSource: DataSource) {
        withContext(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putString(SELECTED_DATA_SOURCE_KEY, dataSource.sourceName)
                apply()
            }
        }
    }

    private companion object {
        const val SELECTED_DATA_SOURCE_KEY = "SELECTED_DATA_SOURCE_KEY"
    }
}