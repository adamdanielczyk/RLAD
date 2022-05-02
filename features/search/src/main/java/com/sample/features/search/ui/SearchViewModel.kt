package com.sample.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: ItemsRepository) : ViewModel() {

    private val _itemsUpdates = MutableSharedFlow<Flow<PagingData<ItemUiModel>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val itemsPagingData = _itemsUpdates.asSharedFlow()

    private val _scrollToTop = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val scrollToTop = _scrollToTop.asSharedFlow()

    private var debounceJob: Job? = null

    init {
        displayAllItems()
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
        scrollToTop()
    }

    fun onClearSearchClicked() {
        displayAllItems()
        scrollToTop()
    }

    private fun scrollToTop() {
        viewModelScope.launch {
            _scrollToTop.emit(Unit)
        }
    }

    private fun performSearch(name: String) {
        postNewPagingData(repository.getItems(name))
    }

    private fun displayAllItems() {
        postNewPagingData(repository.getItems())
    }

    private fun postNewPagingData(newItems: Flow<PagingData<ItemUiModel>>) {
        viewModelScope.launch {
            _itemsUpdates.emit(newItems)
        }
    }

    companion object {
        const val DEBOUNCE_TIME = 400L
    }
}