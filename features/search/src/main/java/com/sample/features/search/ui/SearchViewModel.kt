package com.sample.features.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.sample.domain.model.DataSource
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.AppSettingsRepository
import com.sample.domain.usecase.ResolveItemsRepositoryUseCase
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
    private val resolveItemsRepositoryUseCase: ResolveItemsRepositoryUseCase,
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

    fun onQueryTextChanged(queryText: String?) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(DEBOUNCE_TIME)
            performSearch(queryText.orEmpty())
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

    private suspend fun performSearch(name: String) {
        postNewPagingData(resolveItemsRepositoryUseCase().getItems(name))
    }

    private suspend fun displayAllItems() {
        postNewPagingData(resolveItemsRepositoryUseCase().getItems())
    }

    private suspend fun postNewPagingData(newItems: Flow<PagingData<ItemUiModel>>) {
        _itemsUpdates.emit(newItems)
    }

    fun onGiphyDataSourceClicked() {
        changeToDataSource(DataSource.GIPHY)
    }

    fun onRickAndMortyDataSourceClicked() {
        changeToDataSource(DataSource.RICK_AND_MORTY)
    }

    private fun changeToDataSource(dataSource: DataSource) {
        viewModelScope.launch {
            appSettingsRepository.saveSelectedDataSource(dataSource)
            displayAllItems()
        }
    }

    companion object {
        const val DEBOUNCE_TIME = 400L
    }
}