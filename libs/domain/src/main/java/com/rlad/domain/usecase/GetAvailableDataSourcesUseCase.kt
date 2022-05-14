package com.rlad.domain.usecase

import com.rlad.domain.model.DataSourceUiModel
import kotlinx.coroutines.flow.Flow

interface GetAvailableDataSourcesUseCase {

    operator fun invoke(): Flow<List<DataSourceUiModel>>
}