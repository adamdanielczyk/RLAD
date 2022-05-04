package com.sample.domain.repository

interface AppSettingsRepository {

    suspend fun getSelectedDataSourceName(): String
    suspend fun saveSelectedDataSourceName(dataSourceName: String)
}