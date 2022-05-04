package com.sample.domain.usecase

import com.sample.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface GetItemByIdUseCase {

    suspend operator fun invoke(id: String): Flow<ItemUiModel>
}