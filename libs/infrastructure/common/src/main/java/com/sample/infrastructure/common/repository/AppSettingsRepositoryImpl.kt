package com.sample.infrastructure.common.repository

import android.content.SharedPreferences
import com.sample.domain.repository.AppSettingsRepository
import com.sample.domain.usecase.GetDefaultDataSourceNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AppSettingsRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getDefaultDataSourceNameUseCase: GetDefaultDataSourceNameUseCase,
) : AppSettingsRepository {

    override suspend fun getSelectedDataSourceName(): String = withContext(Dispatchers.IO) {
        val defaultDataSourceName = getDefaultDataSourceNameUseCase()
        sharedPreferences.getString(SELECTED_DATA_SOURCE_KEY, defaultDataSourceName)!!
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