package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.DataSource
import dev.zacsweers.metro.Inject

@Inject
class GetAllDataSourcesUseCase {
    operator fun invoke(): List<DataSource> = DataSource.entries.sortedBy(DataSource::dataSourceName)
}
