package com.sample.infrastructure.common.usecase

import com.sample.domain.model.ItemUiModel
import com.sample.domain.usecase.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetItemByIdUseCaseImpl @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) : GetItemByIdUseCase {

    override suspend operator fun invoke(id: String): Flow<ItemUiModel> =
        getSelectedItemsRepositoryUseCase().getItemById(id)
}