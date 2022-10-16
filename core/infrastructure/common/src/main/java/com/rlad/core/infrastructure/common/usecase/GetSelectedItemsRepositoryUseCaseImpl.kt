package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.repository.AppSettingsRepository
import com.rlad.core.infrastructure.common.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal interface GetSelectedItemsRepositoryUseCase {
    operator fun invoke(): Flow<ItemsRepository>
}

internal class GetSelectedItemsRepositoryUseCaseImpl @Inject constructor(
    private val getAllItemsRepositoriesUseCase: GetAllItemsRepositoriesUseCase,
    private val appSettingsRepository: AppSettingsRepository,
) : GetSelectedItemsRepositoryUseCase {

    override operator fun invoke(): Flow<ItemsRepository> = appSettingsRepository.getSelectedDataSourceName().map { selectedDataSourceName ->
        val allItemsRepositories = getAllItemsRepositoriesUseCase()
        allItemsRepositories.firstOrNull { itemsRepository ->
            itemsRepository.getDataSourceName() == selectedDataSourceName
        } ?: allItemsRepositories.first()
    }
}