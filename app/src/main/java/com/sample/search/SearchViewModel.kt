package com.sample.search

import androidx.lifecycle.ViewModel
import com.sample.data.local.CharacterEntity
import com.sample.data.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchViewModel @Inject constructor(repository: CharacterRepository) : ViewModel() {

    val allCharacters: Flow<List<CharacterEntity>> = repository.getAllCharacters()
        .flowOn(Dispatchers.IO)

    private val _openDetailsScreen = BroadcastChannel<CharacterEntity>(1)
    val openDetailsScreen: Flow<CharacterEntity> = _openDetailsScreen.asFlow()

    fun onItemClicked(entity: CharacterEntity) {
        _openDetailsScreen.offer(entity)
    }
}