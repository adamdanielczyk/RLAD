package com.rlad.core.domain.repository

interface AppSettingsRepository {

    suspend fun saveSelectedDataSourceName(dataSourceName: String)
}