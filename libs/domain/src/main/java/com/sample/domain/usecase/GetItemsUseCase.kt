package com.sample.domain.usecase

import androidx.paging.PagingData
import com.sample.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemsUseCase {

    suspend operator fun invoke(query: String? = null): Flow<PagingData<ItemUiModel>>
}