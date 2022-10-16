package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.repository.ItemsRepository
import javax.inject.Inject

internal interface GetAllItemsRepositoriesUseCase {
    operator fun invoke(): List<ItemsRepository>
}

internal class GetAllItemsRepositoriesUseCaseImpl @Inject constructor(
    private val repositories: Set<@JvmSuppressWildcards ItemsRepository>,
) : GetAllItemsRepositoriesUseCase {

    override operator fun invoke(): List<ItemsRepository> = repositories.sortedBy(ItemsRepository::getDataSourceName)
}