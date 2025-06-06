package com.rlad.core.domain.usecase

import androidx.paging.PagingData
import com.rlad.core.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemsUseCase {

    operator fun invoke(query: String? = null): Flow<PagingData<ItemUiModel>>
}