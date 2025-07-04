package com.rlad.composeapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rlad.shared.domain.model.DataSourceUiModel
import com.rlad.shared.domain.model.ItemUiModel
import com.rlad.shared.domain.repository.AppSettingsRepository
import com.rlad.shared.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.shared.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    val getAvailableDataSourcesUseCase: GetAvailableDataSourcesUseCase,
    private val getItemsUseCase: GetItemsUseCase,
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    private val _itemsUpdates = MutableStateFlow<Flow<List<ItemUiModel>>?>(value = null)
    val itemsPagingData = _itemsUpdates.asStateFlow()

    private val _scrollToTop = Channel<Unit>(Channel.BUFFERED)
    val scrollToTop = _scrollToTop.receiveAsFlow()

    private val _clearSearch = Channel<Unit>(Channel.BUFFERED)
    val clearSearch = _clearSearch.receiveAsFlow()

    private var debounceJob: Job? = null

    init {
        viewModelScope.launch {
            displayAllItems()
        }
    }

    fun onQueryTextChanged(query: String) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(DEBOUNCE_TIME)
            performSearch(query)
        }
    }

    fun onScrollToTopClicked() {
        viewModelScope.launch {
            scrollToTop()
        }
    }

    fun onClearSearchClicked() {
        viewModelScope.launch {
            displayAllItems()
            scrollToTop()
        }
    }

    private suspend fun scrollToTop() {
        _scrollToTop.send(Unit)
    }

    private suspend fun performSearch(query: String) {
        postNewPagingData(getItemsUseCase(query))
    }

    private suspend fun displayAllItems() {
        postNewPagingData(getItemsUseCase())
    }

    private suspend fun postNewPagingData(newItems: Flow<List<ItemUiModel>>) {
        _itemsUpdates.emit(newItems)
    }

    fun onDataSourceClicked(dataSource: DataSourceUiModel) {
        viewModelScope.launch {
            _clearSearch.send(Unit)
            appSettingsRepository.saveSelectedDataSourceName(dataSource.name)
            displayAllItems()
        }
    }

    companion object {
        const val DEBOUNCE_TIME = 400L
    }
}