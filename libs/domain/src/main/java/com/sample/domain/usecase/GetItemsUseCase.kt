package com.sample.domain.usecase

import androidx.paging.PagingData
import com.sample.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    private val getSelectedItemsRepositoryUseCase: GetSelectedItemsRepositoryUseCase,
) {

    suspend operator fun invoke(name: String? = null): Flow<PagingData<ItemUiModel>> =
        getSelectedItemsRepositoryUseCase().getItems(name)
}