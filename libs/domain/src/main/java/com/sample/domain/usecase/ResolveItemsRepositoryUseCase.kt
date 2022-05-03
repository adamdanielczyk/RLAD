package com.sample.domain.usecase

import com.sample.domain.model.DataSource
import com.sample.domain.repository.AppSettingsRepository
import com.sample.domain.repository.ItemsRepository
import javax.inject.Inject

class ResolveItemsRepositoryUseCase @Inject constructor(
    private val repositories: Map<DataSource, @JvmSuppressWildcards ItemsRepository>,
    private val appSettingsRepository: AppSettingsRepository,
) {

    suspend operator fun invoke(): ItemsRepository {
        val selectedDataSource = appSettingsRepository.getSelectedDataSource()
        return repositories.getValue(selectedDataSource)
    }
}