package com.rlad.shared.domain.usecase

import com.rlad.shared.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemsUseCase {
    operator fun invoke(query: String? = null): Flow<List<ItemUiModel>>
}