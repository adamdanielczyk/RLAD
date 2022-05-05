package com.rlad.domain.repository

interface AppSettingsRepository {

    suspend fun getSelectedDataSourceName(): String?
    suspend fun saveSelectedDataSourceName(dataSourceName: String)
}