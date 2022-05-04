package com.sample.domain.usecase

import com.sample.domain.model.DataSourceUiModel

interface GetAvailableDataSourcesUseCase {

    operator fun invoke(): List<DataSourceUiModel>
}