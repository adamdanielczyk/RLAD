package com.rlad.shared.data.common.repository

import com.rlad.shared.domain.repository.AppSettingsRepository

internal class AppSettingsRepositoryImpl : AppSettingsRepository {

    override suspend fun saveSelectedDataSourceName(dataSourceName: String) {
        // Simplified implementation - database integration can be added later
        println("Selected data source: $dataSourceName")
    }
}