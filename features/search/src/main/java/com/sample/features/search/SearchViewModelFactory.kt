package com.sample.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.core.data.repository.CharacterRepository
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val repository: CharacterRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SearchViewModel::class.java) {
            error("Unknown ViewModel class: $modelClass")
        }
        return SearchViewModel(repository) as T
    }
}