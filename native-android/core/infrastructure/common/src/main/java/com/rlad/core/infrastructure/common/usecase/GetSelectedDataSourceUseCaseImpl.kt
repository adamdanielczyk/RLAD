package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.repository.AppSettingsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal interface GetSelectedDataSourceUseCase {
    operator fun invoke(): Flow<DataSource>
}

internal class GetSelectedDataSourceUseCaseImpl @Inject constructor(
    private val getAllDataSourcesUseCase: GetAllDataSourcesUseCase,
    private val appSettingsRepository: AppSettingsRepositoryImpl,
) : GetSelectedDataSourceUseCase {

    override operator fun invoke(): Flow<DataSource> = appSettingsRepository.getSelectedDataSource().map { selectedDataSourceName ->
        val allDataSources = getAllDataSourcesUseCase()
        allDataSources.firstOrNull { dataSource ->
            dataSource == selectedDataSourceName
        } ?: DataSource.GIPHY
    }
}
