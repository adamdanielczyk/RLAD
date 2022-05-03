package com.sample.features.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.domain.model.ItemUiModel
import com.sample.domain.usecase.ResolveItemsRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val resolveItemsRepositoryUseCase: ResolveItemsRepositoryUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _item = MutableStateFlow<ItemUiModel?>(value = null)
    val item = _item.asStateFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id")!!
            val itemFlow = resolveItemsRepositoryUseCase().getItemBy(id)
            itemFlow.collectLatest(_item::emit)
        }
    }
}