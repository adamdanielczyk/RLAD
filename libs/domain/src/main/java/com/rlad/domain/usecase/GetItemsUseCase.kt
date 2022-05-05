package com.rlad.domain.usecase

import androidx.paging.PagingData
import com.rlad.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemsUseCase {

    suspend operator fun invoke(query: String? = null): Flow<PagingData<ItemUiModel>>
}