package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

internal class GetItemByIdUseCaseImpl @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) : GetItemByIdUseCase {

    override operator fun invoke(id: String): Flow<ItemUiModel> =
        getSelectedItemsRepositoryUseCase().flatMapConcat { itemsRepository -> itemsRepository.getItemById(id) }
}