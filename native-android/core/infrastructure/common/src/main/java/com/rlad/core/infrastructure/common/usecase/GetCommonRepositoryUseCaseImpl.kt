package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal interface GetCommonRepositoryUseCase {
    operator fun invoke(): Flow<CommonRepository>
}

internal class GetCommonRepositoryUseCaseImpl @Inject constructor(
    private val getSelectedDataSourceUseCase: GetSelectedDataSourceUseCase,
    private val commonRepositories: Map<DataSource, @JvmSuppressWildcards CommonRepository>,
) : GetCommonRepositoryUseCase {

    override operator fun invoke(): Flow<CommonRepository> = getSelectedDataSourceUseCase().map { dataSource ->
        commonRepositories.getValue(dataSource)
    }
}