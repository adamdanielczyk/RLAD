package com.rlad.domain.usecase

import com.rlad.domain.model.DataSourceUiModel

interface GetAvailableDataSourcesUseCase {

    operator fun invoke(): List<DataSourceUiModel>
}