package com.sample.domain.usecase

import com.sample.domain.model.DataSource
import com.sample.domain.repository.ItemsRepository
import javax.inject.Inject

class ResolveItemsRepositoryUseCase @Inject constructor(
    private val repositories: Map<DataSource, @JvmSuppressWildcards ItemsRepository>,
) {

    operator fun invoke(): ItemsRepository {
        return repositories.getValue(DataSource.GIPHY)
    }
}