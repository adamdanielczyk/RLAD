package com.rlad.shared.domain.usecase.impl

import com.rlad.shared.data.rickandmorty.remote.RickAndMortyApi
import com.rlad.shared.domain.model.ItemUiModel
import com.rlad.shared.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GetItemsUseCaseImpl(
    private val rickAndMortyApi: RickAndMortyApi
) : GetItemsUseCase {

    override operator fun invoke(query: String?): Flow<List<ItemUiModel>> = flow {
        try {
            val response = rickAndMortyApi.getCharacters(page = 1, name = query)
            val characters = response.results.map { character ->
                ItemUiModel(
                    id = character.id.toString(),
                    imageUrl = character.imageUrl,
                    name = character.name,
                    cardCaption = "${character.species} - ${character.status}",
                    detailsKeyValues = listOf(
                        "Name" to character.name,
                        "Status" to character.status.name,
                        "Species" to character.species,
                        "Type" to character.type,
                        "Gender" to character.gender.name,
                        "Location" to character.location.name,
                        "Created" to character.created
                    )
                )
            }
            emit(characters)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}