package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.DataSource
import com.rlad.core.infrastructure.common.repository.CommonRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetCommonRepositoryUseCase {
    operator fun invoke(): Flow<CommonRepository>
}

@Inject
@ContributesBinding(AppScope::class)
class GetCommonRepositoryUseCaseImpl(
    private val getSelectedDataSourceUseCase: GetSelectedDataSourceUseCase,
    private val commonRepositories: Map<DataSource, CommonRepository>,
) : GetCommonRepositoryUseCase {

    override operator fun invoke(): Flow<CommonRepository> = getSelectedDataSourceUseCase().map { dataSource ->
        commonRepositories.getValue(dataSource)
    }
}
