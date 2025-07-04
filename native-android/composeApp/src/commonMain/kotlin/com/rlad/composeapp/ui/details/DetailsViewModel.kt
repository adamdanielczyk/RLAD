package com.rlad.composeapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rlad.shared.domain.model.ItemUiModel
import com.rlad.shared.domain.usecase.GetItemByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val itemId: String,
    private val getItemByIdUseCase: GetItemByIdUseCase
) : ViewModel() {
    
    private val _item = MutableStateFlow<ItemUiModel?>(null)
    val item = _item.asStateFlow()
    
    init {
        loadItem()
    }
    
    private fun loadItem() {
        viewModelScope.launch {
            getItemByIdUseCase(itemId).collect { itemData ->
                _item.value = itemData
            }
        }
    }
}