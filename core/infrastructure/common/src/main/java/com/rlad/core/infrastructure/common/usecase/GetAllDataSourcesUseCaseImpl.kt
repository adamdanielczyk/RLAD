package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.model.DataSource
import javax.inject.Inject

internal interface GetAllDataSourcesUseCase {
    operator fun invoke(): List<DataSource>
}

internal class GetAllDataSourcesUseCaseImpl @Inject constructor() : GetAllDataSourcesUseCase {

    override operator fun invoke(): List<DataSource> = DataSource.values().sortedBy(DataSource::dataSourceName)
}