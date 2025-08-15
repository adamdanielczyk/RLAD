package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Inject
class GetCommonRepositoryUseCase(
    private val getSelectedDataSourceUseCase: GetSelectedDataSourceUseCase,
    private val commonRepositories: Map<DataSource, @JvmSuppressWildcards CommonRepository>,
) {

    operator fun invoke(): Flow<CommonRepository> = getSelectedDataSourceUseCase().map { dataSource ->
        commonRepositories.getValue(dataSource)
    }
}
