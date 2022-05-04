package com.sample.domain.usecase

import javax.inject.Inject

class GetDefaultDataSourceNameUseCase @Inject constructor(
    private val getAllItemsRepositoriesUseCase: GetAllItemsRepositoriesUseCase,
) {

    operator fun invoke(): String = getAllItemsRepositoriesUseCase().first().getDataSourceName()
}