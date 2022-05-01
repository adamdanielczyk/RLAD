package com.sample.features.details

import androidx.lifecycle.ViewModel
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: CharacterRepository) : ViewModel() {

    fun getCharacter(characterId: Int): Flow<CharacterEntity> = repository.getCharacterBy(characterId)
}