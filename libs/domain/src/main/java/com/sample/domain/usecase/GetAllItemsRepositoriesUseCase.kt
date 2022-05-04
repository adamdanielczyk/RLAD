package com.sample.domain.usecase

import com.sample.domain.repository.ItemsRepository
import javax.inject.Inject

class GetAllItemsRepositoriesUseCase @Inject constructor(
    private val repositories: Set<@JvmSuppressWildcards ItemsRepository>,
) {

    operator fun invoke(): Set<ItemsRepository> = repositories
}