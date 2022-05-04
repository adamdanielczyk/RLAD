package com.sample.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.sample.domain.model.DataSourceUiModel
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.AppSettingsRepository
import com.sample.domain.usecase.GetAvailableDataSourcesUseCase
import com.sample.domain.usecase.GetItemsUseCase
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
internal class SearchViewModel @Inject constructor(
    val getAvailableDataSourcesUseCase: GetAvailableDataSourcesUseCase,
    private val getItemsUseCase: GetItemsUseCase,
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    private val _itemsUpdates = MutableSharedFlow<Flow<PagingData<ItemUiModel>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val itemsPagingData = _itemsUpdates.asSharedFlow()

    private val _scrollToTop = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val scrollToTop = _scrollToTop.asSharedFlow()

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
        _scrollToTop.emit(Unit)
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