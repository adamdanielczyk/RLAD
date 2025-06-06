package com.rlad.core.domain.usecase

import com.rlad.core.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemByIdUseCase {

    operator fun invoke(id: String): Flow<ItemUiModel>
}