package com.rlad.shared.domain.usecase.impl

import com.rlad.shared.domain.model.ItemUiModel
import com.rlad.shared.domain.usecase.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class GetItemByIdUseCaseImpl : GetItemByIdUseCase {

    override operator fun invoke(id: String): Flow<ItemUiModel> = flowOf(
        ItemUiModel(
            id = id,
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/$id.jpeg",
            name = "Character $id",
            cardCaption = "Sample character",
            detailsKeyValues = listOf(
                "ID" to id,
                "Status" to "Alive",
                "Species" to "Human"
            )
        )
    )
}