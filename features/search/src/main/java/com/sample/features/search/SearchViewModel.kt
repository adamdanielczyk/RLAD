package com.sample.features.search

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: CharacterRepository) : ViewModel() {

    private val _charactersUpdates = MutableSharedFlow<Flow<PagingData<CharacterEntity>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val charactersUpdates = _charactersUpdates.asSharedFlow()

    private val _openDetailsScreen = MutableSharedFlow<Pair<CharacterEntity, ImageView>>(
        extraBufferCapacity = 1
    )
    val openDetailsScreen = _openDetailsScreen.asSharedFlow()

    private val _scrollToTop = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val scrollToTop = _scrollToTop.asSharedFlow()

    private var debounceJob: Job? = null

    init {
        displayAllCharacters()
    }

    fun onItemClicked(entity: CharacterEntity, imageView: ImageView) {
        _openDetailsScreen.tryEmit(entity to imageView)
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
        _scrollToTop.tryEmit(Unit)
    }

    private fun performSearch(nameOrLocation: String) {
        postNewPagingData(repository.getCharacters(nameOrLocation))
    }

    private fun displayAllCharacters() {
        postNewPagingData(repository.getCharacters())
    }

    private fun postNewPagingData(newCharacters: Flow<PagingData<CharacterEntity>>) {
        _charactersUpdates.tryEmit(newCharacters)
    }

    companion object {
        const val DEBOUNCE_TIME = 400L
    }
}