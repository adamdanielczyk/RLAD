package com.rlad.infrastructure.common.usecase

import com.rlad.domain.model.ItemUiModel
import com.rlad.domain.usecase.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

internal class GetItemByIdUseCaseImpl @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) : GetItemByIdUseCase {

    override operator fun invoke(id: String): Flow<ItemUiModel> =
        getSelectedItemsRepositoryUseCase().flatMapLatest { itemsRepository -> itemsRepository.getItemById(id) }
}