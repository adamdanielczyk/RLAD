package com.sample.infrastructure.common.usecase

import com.sample.domain.model.DataSourceUiModel
import com.sample.domain.usecase.GetAvailableDataSourcesUseCase
import javax.inject.Inject

internal class GetAvailableDataSourcesUseCaseImpl @Inject constructor(
    private val getAllItemsRepositoriesUseCase: GetAllItemsRepositoriesUseCase,
) : GetAvailableDataSourcesUseCase {

    override operator fun invoke(): List<DataSourceUiModel> = getAllItemsRepositoriesUseCase().map { itemsRepository ->
        DataSourceUiModel(
            name = itemsRepository.getDataSourceName(),
            pickerText = itemsRepository.getDataSourcePickerText(),
        )
    }
}