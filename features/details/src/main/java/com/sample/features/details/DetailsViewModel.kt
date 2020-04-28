package com.sample.features.details

import androidx.lifecycle.ViewModel
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class DetailsViewModel(
    characterId: Int,
    repository: CharacterRepository
) : ViewModel() {

    val character: Flow<CharacterEntity> = repository.getCharacterById(characterId)
}