package com.sample.features.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    fun getCharacter(): Flow<CharacterEntity> {
        val characterId = savedStateHandle.get<String>("id")!!.toInt()
        return repository.getCharacterBy(characterId)
    }
}