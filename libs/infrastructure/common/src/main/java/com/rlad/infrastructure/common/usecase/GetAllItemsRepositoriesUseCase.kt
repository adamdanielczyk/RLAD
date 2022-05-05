package com.rlad.infrastructure.common.usecase

import com.rlad.infrastructure.common.repository.ItemsRepository
import javax.inject.Inject

internal class GetAllItemsRepositoriesUseCase @Inject constructor(
    private val repositories: Set<@JvmSuppressWildcards ItemsRepository>,
) {

    operator fun invoke(): List<ItemsRepository> = repositories.sortedBy(ItemsRepository::getDataSourceName)
}