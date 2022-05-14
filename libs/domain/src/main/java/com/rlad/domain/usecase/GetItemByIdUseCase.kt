package com.rlad.domain.usecase

import com.rlad.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemByIdUseCase {

    operator fun invoke(id: String): Flow<ItemUiModel>
}