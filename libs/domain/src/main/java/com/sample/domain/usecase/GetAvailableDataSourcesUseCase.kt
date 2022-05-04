package com.sample.domain.usecase

import com.sample.domain.model.DataSourceUiModel
import javax.inject.Inject

class GetAvailableDataSourcesUseCase @Inject constructor(
    private val getAllItemsRepositoriesUseCase: GetAllItemsRepositoriesUseCase,
) {

    operator fun invoke(): List<DataSourceUiModel> = getAllItemsRepositoriesUseCase().map { itemsRepository ->
        DataSourceUiModel(
            name = itemsRepository.getDataSourceName(),
            pickerTextResId = itemsRepository.getDataSourcePickerTextResId(),
        )
    }
}