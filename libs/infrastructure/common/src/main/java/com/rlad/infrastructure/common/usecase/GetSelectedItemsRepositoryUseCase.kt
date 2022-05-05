package com.rlad.infrastructure.common.usecase

import com.rlad.domain.repository.AppSettingsRepository
import com.rlad.infrastructure.common.repository.ItemsRepository
import javax.inject.Inject

internal class GetSelectedItemsRepositoryUseCase @Inject constructor(
    private val getAllItemsRepositoriesUseCase: GetAllItemsRepositoriesUseCase,
    private val appSettingsRepository: AppSettingsRepository,
) {

    suspend operator fun invoke(): ItemsRepository {
        val selectedDataSourceName = appSettingsRepository.getSelectedDataSourceName()
        val allItemsRepositories = getAllItemsRepositoriesUseCase()
        return allItemsRepositories.firstOrNull { itemsRepository ->
            itemsRepository.getDataSourceName() == selectedDataSourceName
        } ?: allItemsRepositories.first()
    }
}