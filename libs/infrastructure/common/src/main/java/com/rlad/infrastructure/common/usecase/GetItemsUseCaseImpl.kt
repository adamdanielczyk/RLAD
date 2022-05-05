package com.rlad.infrastructure.common.usecase

import androidx.paging.PagingData
import com.rlad.domain.model.ItemUiModel
import com.rlad.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetItemsUseCaseImpl @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) : GetItemsUseCase {

    override suspend operator fun invoke(query: String?): Flow<PagingData<ItemUiModel>> =
        getSelectedItemsRepositoryUseCase().getItems(query)
}