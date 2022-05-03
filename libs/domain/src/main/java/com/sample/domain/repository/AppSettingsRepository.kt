package com.sample.domain.repository

import com.sample.domain.model.DataSource

interface AppSettingsRepository {

    suspend fun getSelectedDataSource(): DataSource
    suspend fun saveSelectedDataSource(dataSource: DataSource)
}