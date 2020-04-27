package com.sample.search

import androidx.lifecycle.ViewModel
import com.sample.data.local.CharacterEntity
import com.sample.data.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchViewModel @Inject constructor(repository: CharacterRepository) : ViewModel() {

    val allCharacters: Flow<List<CharacterEntity>> = repository.getAllCharacters()
        .flowOn(Dispatchers.IO)
}