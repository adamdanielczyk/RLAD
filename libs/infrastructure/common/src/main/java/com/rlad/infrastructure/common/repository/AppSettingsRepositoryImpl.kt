package com.rlad.infrastructure.common.repository

import android.content.SharedPreferences
import com.rlad.domain.repository.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AppSettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : AppSettingsRepository {

    override suspend fun getSelectedDataSourceName(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(SELECTED_DATA_SOURCE_KEY, null)
    }

    override suspend fun saveSelectedDataSourceName(dataSourceName: String) {
        withContext(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                putString(SELECTED_DATA_SOURCE_KEY, dataSourceName)
                apply()
            }
        }
    }

    private companion object {
        const val SELECTED_DATA_SOURCE_KEY = "SELECTED_DATA_SOURCE_KEY"
    }
}