package com.sample.infrastructure.common.usecase

import com.sample.domain.model.DataSourceUiModel
import com.sample.domain.usecase.GetAvailableDataSourcesUseCase
import com.sample.infrastructure.common.repository.ItemsRepository
import javax.inject.Inject

internal class GetAvailableDataSourcesUseCaseImpl @Inject constructor(
    private val repositories: Set<@JvmSuppressWildcards ItemsRepository>,
) : GetAvailableDataSourcesUseCase {

    override operator fun invoke(): List<DataSourceUiModel> = repositories.map { itemsRepository ->
        DataSourceUiModel(
            name = itemsRepository.getDataSourceName(),
            pickerTextResId = itemsRepository.getDataSourcePickerTextResId(),
        )
    }
}