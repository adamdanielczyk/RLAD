package com.sample.domain.usecase

import com.sample.domain.repository.AppSettingsRepository
import com.sample.domain.repository.ItemsRepository
import javax.inject.Inject

class GetSelectedItemsRepositoryUseCase @Inject constructor(
    private val getAllItemsRepositoriesUseCase: GetAllItemsRepositoriesUseCase,
    private val appSettingsRepository: AppSettingsRepository,
) {

    suspend operator fun invoke(): ItemsRepository {
        val selectedDataSourceName = appSettingsRepository.getSelectedDataSourceName()
        return getAllItemsRepositoriesUseCase().first { it.getDataSourceName() == selectedDataSourceName }
    }
}