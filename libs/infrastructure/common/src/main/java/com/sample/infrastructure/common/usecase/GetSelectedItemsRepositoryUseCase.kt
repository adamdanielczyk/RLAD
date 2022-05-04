package com.sample.infrastructure.common.usecase

import com.sample.domain.repository.AppSettingsRepository
import com.sample.infrastructure.common.repository.ItemsRepository
import javax.inject.Inject

internal class GetSelectedItemsRepositoryUseCase @Inject constructor(
    private val repositories: Set<@JvmSuppressWildcards ItemsRepository>,
    private val appSettingsRepository: AppSettingsRepository,
) {

    suspend operator fun invoke(): ItemsRepository {
        val selectedDataSourceName = appSettingsRepository.getSelectedDataSourceName()
        return repositories.firstOrNull { itemsRepository ->
            itemsRepository.getDataSourceName() == selectedDataSourceName
        } ?: repositories.minByOrNull(ItemsRepository::getDataSourceName)!!
    }
}