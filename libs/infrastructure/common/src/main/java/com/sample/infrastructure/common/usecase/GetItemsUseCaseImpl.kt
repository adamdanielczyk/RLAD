package com.sample.infrastructure.common.usecase

import androidx.paging.PagingData
import com.sample.domain.model.ItemUiModel
import com.sample.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetItemsUseCaseImpl @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) : GetItemsUseCase {

    override suspend operator fun invoke(query: String?): Flow<PagingData<ItemUiModel>> =
        getSelectedItemsRepositoryUseCase().getItems(query)
}