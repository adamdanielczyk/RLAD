package com.sample.domain.usecase

import com.sample.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) {

    suspend operator fun invoke(id: String): Flow<ItemUiModel> =
        getSelectedItemsRepositoryUseCase().getItemById(id)
}