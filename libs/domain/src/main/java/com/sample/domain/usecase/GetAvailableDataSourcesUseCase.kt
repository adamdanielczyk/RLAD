package com.sample.domain.usecase

import com.sample.domain.model.DataSourceUiModel
import com.sample.domain.repository.ItemsRepository
import javax.inject.Inject

class GetAvailableDataSourcesUseCase @Inject constructor(
    private val repositories: Set<@JvmSuppressWildcards ItemsRepository>,
) {

    operator fun invoke(): List<DataSourceUiModel> = repositories.map { itemsRepository ->
        DataSourceUiModel(
            name = itemsRepository.getDataSourceName(),
            pickerTextResId = itemsRepository.getDataSourcePickerTextResId(),
        )
    }
}