package com.sample.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.core.data.repository.CharacterRepository
import javax.inject.Inject

class DetailsViewModelFactory @Inject constructor(
    private val characterId: Int,
    private val repository: CharacterRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != DetailsViewModel::class.java) {
            error("Unknown ViewModel class: $modelClass")
        }
        return DetailsViewModel(
            characterId,
            repository
        ) as T
    }
}