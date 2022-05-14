package com.rlad.infrastructure.common.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rlad.domain.repository.AppSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("app_settings")

internal class AppSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppSettingsRepository {

    private val settingsDataStore = context.dataStore

    override fun getSelectedDataSourceName(): Flow<String?> = settingsDataStore.data.map { settings ->
        settings[SELECTED_DATA_SOURCE_KEY]
    }

    override suspend fun saveSelectedDataSourceName(dataSourceName: String) {
        settingsDataStore.edit { settings ->
            settings[SELECTED_DATA_SOURCE_KEY] = dataSourceName
        }
    }

    private companion object {
        val SELECTED_DATA_SOURCE_KEY = stringPreferencesKey("SELECTED_DATA_SOURCE_KEY")
    }
}