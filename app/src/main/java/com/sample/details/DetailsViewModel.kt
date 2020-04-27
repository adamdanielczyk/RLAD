package com.sample.details

import androidx.lifecycle.ViewModel
import com.sample.data.local.CharacterEntity
import com.sample.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    lateinit var character: Flow<CharacterEntity>

    fun init(characterId: Int) {
        character = repository.getCharacterById(characterId)
    }
}