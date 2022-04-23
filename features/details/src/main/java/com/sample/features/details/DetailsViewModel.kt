package com.sample.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DetailsViewModel @AssistedInject constructor(
    repository: CharacterRepository,
    @Assisted characterId: Int,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(characterId: Int): DetailsViewModel
    }

    val character: Flow<CharacterEntity> = repository.getCharacterBy(characterId)

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            factory: Factory,
            characterId: Int,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(characterId) as T
            }
        }
    }
}