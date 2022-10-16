package com.rlad.core.infrastructure.common.usecase

import androidx.paging.PagingData
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

internal class GetItemsUseCaseImpl @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) : GetItemsUseCase {

    override operator fun invoke(query: String?): Flow<PagingData<ItemUiModel>> =
        getSelectedItemsRepositoryUseCase().flatMapConcat { itemsRepository ->
            if (query != null) {
                itemsRepository.getSearchItems(query)
            } else {
                itemsRepository.getAllItems()
            }
        }
}