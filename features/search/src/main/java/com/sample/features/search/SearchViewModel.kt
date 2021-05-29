package com.sample.features.search

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: CharacterRepository) : ViewModel() {

    private val _charactersUpdates = ConflatedBroadcastChannel<Flow<PagingData<CharacterEntity>>>()
    val charactersUpdates = _charactersUpdates.asFlow()

    private val _openDetailsScreen = BroadcastChannel<Pair<CharacterEntity, ImageView>>(1)
    val openDetailsScreen = _openDetailsScreen.asFlow()

    private val _scrollToTop = BroadcastChannel<Unit>(1)
    val scrollToTop = _scrollToTop.asFlow()

    private var debounceJob: Job? = null

    init {
        displayAllCharacters()
    }

    fun onItemClicked(entity: CharacterEntity, imageView: ImageView) {
        _openDetailsScreen.offer(entity to imageView)
    }

    fun onQueryTextChanged(queryText: String?) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(DEBOUNCE_TIME)
            performSearch(queryText.orEmpty())
        }
    }

    fun onSearchExpanded() {
        scrollToTop()
    }

    fun onSearchCollapsed() {
        displayAllCharacters()
        scrollToTop()
    }

    fun onClearSearchClicked() {
        scrollToTop()
    }

    private fun scrollToTop() {
        _scrollToTop.offer(Unit)
    }

    private fun performSearch(nameOrLocation: String) {
        postNewPagingData(repository.getCharacters(nameOrLocation))
    }

    private fun displayAllCharacters() {
        postNewPagingData(repository.getCharacters())
    }

    private fun postNewPagingData(newCharacters: Flow<PagingData<CharacterEntity>>) {
        _charactersUpdates.offer(newCharacters)
    }

    companion object {
        const val DEBOUNCE_TIME = 400L
    }
}