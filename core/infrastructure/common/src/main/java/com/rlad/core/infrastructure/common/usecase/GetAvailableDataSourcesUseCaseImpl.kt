package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.DataSourceUiModel
import com.rlad.core.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAvailableDataSourcesUseCaseImpl @Inject constructor(
    private val getAllDataSourcesUseCase: GetAllDataSourcesUseCase,
    private val getSelectedDataSourceUseCase: GetSelectedDataSourceUseCase,
    private val dataSourceConfigurations: Map<DataSource, @JvmSuppressWildcards DataSourceConfiguration>,
) : GetAvailableDataSourcesUseCase {

    override operator fun invoke(): Flow<List<DataSourceUiModel>> = getSelectedDataSourceUseCase().map { selectedDataSource ->
        getAllDataSourcesUseCase().map { dataSource ->
            val dataSourceConfiguration = dataSourceConfigurations.getValue(dataSource)
            DataSourceUiModel(
                name = dataSource.dataSourceName,
                pickerText = dataSourceConfiguration.getDataSourcePickerText(),
                isSelected = dataSource == selectedDataSource,
            )
        }
    }
}