package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.repository.AppSettingsRepositoryImpl
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Inject
class GetSelectedDataSourceUseCase(
    private val getAllDataSourcesUseCase: GetAllDataSourcesUseCase,
    private val appSettingsRepository: AppSettingsRepositoryImpl,
) {

    operator fun invoke(): Flow<DataSource> = appSettingsRepository.getSelectedDataSource().map { selectedDataSourceName ->
        val allDataSources = getAllDataSourcesUseCase()
        allDataSources.firstOrNull { dataSource ->
            dataSource == selectedDataSourceName
        } ?: DataSource.GIPHY
    }
}
