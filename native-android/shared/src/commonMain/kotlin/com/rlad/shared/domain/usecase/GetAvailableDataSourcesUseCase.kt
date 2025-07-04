package com.rlad.shared.domain.usecase

import com.rlad.shared.domain.model.DataSourceUiModel
import kotlinx.coroutines.flow.Flow

interface GetAvailableDataSourcesUseCase {
    operator fun invoke(): Flow<List<DataSourceUiModel>>
}