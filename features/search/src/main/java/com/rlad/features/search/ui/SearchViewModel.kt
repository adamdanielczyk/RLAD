package com.rlad.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.rlad.domain.model.DataSourceUiModel
import com.rlad.domain.model.ItemUiModel
import com.rlad.domain.repository.AppSettingsRepository
import com.rlad.domain.usecase.GetAvailableDataSourcesUseCase
import com.rlad.domain.usecase.GetItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    val getAvailableDataSourcesUseCase: GetAvailableDataSourcesUseCase,
    private val getItemsUseCase: GetItemsUseCase,
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    private val _itemsUpdates = MutableStateFlow<Flow<PagingData<ItemUiModel>>?>(value = null)
    val itemsPagingData = _itemsUpdates.asStateFlow()

    private val _scrollToTop = Channel<Unit>(Channel.BUFFERED)
    val scrollToTop = _scrollToTop.receiveAsFlow()

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

    fun onSearchExpanded() {
        viewModelScope.launch {
            scrollToTop()
        }
    }

    fun onSearchCollapsed() {
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

    private suspend fun postNewPagingData(newItems: Flow<PagingData<ItemUiModel>>) {
        _itemsUpdates.emit(newItems)
    }

    fun onDataSourceClicked(dataSource: DataSourceUiModel) {
        viewModelScope.launch {
            appSettingsRepository.saveSelectedDataSourceName(dataSource.name)
            displayAllItems()
        }
    }

    companion object {
        const val DEBOUNCE_TIME = 400L
    }
}