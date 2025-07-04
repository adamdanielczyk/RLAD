package com.rlad.shared.domain.repository

interface AppSettingsRepository {
    suspend fun saveSelectedDataSourceName(dataSourceName: String)
}