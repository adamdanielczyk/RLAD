package com.sample.features.search

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class SearchViewModel(repository: CharacterRepository) : ViewModel() {

    private val _allCharacters: LiveData<PagedList<CharacterEntity>> =
        repository.getAllCharacters(viewModelScope)
    val allCharacters = _allCharacters.asFlow()

    private val _openDetailsScreen = BroadcastChannel<Pair<CharacterEntity, ImageView>>(1)
    val openDetailsScreen: Flow<Pair<CharacterEntity, ImageView>> = _openDetailsScreen.asFlow()

    fun onItemClicked(entity: CharacterEntity, imageView: ImageView) {
        _openDetailsScreen.offer(entity to imageView)
    }
}