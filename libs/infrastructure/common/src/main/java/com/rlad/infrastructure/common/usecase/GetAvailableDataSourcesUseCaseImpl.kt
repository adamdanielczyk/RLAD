package com.rlad.infrastructure.common.usecase

import com.rlad.domain.model.DataSourceUiModel
import com.rlad.domain.usecase.GetAvailableDataSourcesUseCase
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