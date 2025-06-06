package com.rlad.core.domain.usecase

import com.rlad.core.domain.model.DataSourceUiModel
import kotlinx.coroutines.flow.Flow

interface GetAvailableDataSourcesUseCase {

    operator fun invoke(): Flow<List<DataSourceUiModel>>
}