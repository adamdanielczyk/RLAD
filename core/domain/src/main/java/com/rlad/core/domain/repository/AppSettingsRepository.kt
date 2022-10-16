package com.rlad.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    fun getSelectedDataSourceName(): Flow<String?>
    suspend fun saveSelectedDataSourceName(dataSourceName: String)
}