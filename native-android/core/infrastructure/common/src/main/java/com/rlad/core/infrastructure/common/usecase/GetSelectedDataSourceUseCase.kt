package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.DataSource
import com.rlad.core.infrastructure.common.repository.AppSettingsRepositoryImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetSelectedDataSourceUseCase {
    operator fun invoke(): Flow<DataSource>
}

@ContributesBinding(AppScope::class)
class GetSelectedDataSourceUseCaseImpl(
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
