package com.rlad.shared.domain.usecase.impl

import com.rlad.shared.domain.model.DataSourceUiModel
import com.rlad.shared.domain.usecase.GetAvailableDataSourcesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class GetAvailableDataSourcesUseCaseImpl : GetAvailableDataSourcesUseCase {

    override operator fun invoke(): Flow<List<DataSourceUiModel>> = flowOf(
        listOf(
            DataSourceUiModel(
                name = "RICK_AND_MORTY",
                pickerText = "Rick and Morty Characters",
                isSelected = true
            ),
            DataSourceUiModel(
                name = "GIPHY",
                pickerText = "Giphy GIFs",
                isSelected = false
            )
        )
    )
}