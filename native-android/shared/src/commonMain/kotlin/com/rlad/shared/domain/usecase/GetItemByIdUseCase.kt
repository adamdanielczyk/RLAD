package com.rlad.shared.domain.usecase

import com.rlad.shared.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemByIdUseCase {
    operator fun invoke(id: String): Flow<ItemUiModel>
}