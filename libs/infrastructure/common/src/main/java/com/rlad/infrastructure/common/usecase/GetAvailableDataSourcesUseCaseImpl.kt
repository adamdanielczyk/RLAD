package com.rlad.infrastructure.common.usecase

import com.rlad.domain.model.DataSourceUiModel
import com.rlad.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.infrastructure.common.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAvailableDataSourcesUseCaseImpl @Inject constructor(
    private val getAllItemsRepositoriesUseCase: GetAllItemsRepositoriesUseCase,
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) : GetAvailableDataSourcesUseCase {

    override operator fun invoke(): Flow<List<DataSourceUiModel>> = getSelectedItemsRepositoryUseCase()
        .map(ItemsRepository::getDataSourceName)
        .map { selectedDataSourceName ->
            getAllItemsRepositoriesUseCase().map { itemsRepository ->
                val dataSourceName = itemsRepository.getDataSourceName()
                DataSourceUiModel(
                    name = dataSourceName,
                    pickerText = itemsRepository.getDataSourcePickerText(),
                    isSelected = dataSourceName == selectedDataSourceName,
                )
            }
        }
}