package com.rlad.feature.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    getItemByIdUseCase: GetItemByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val item: Flow<ItemUiModel>

    init {
        val id = savedStateHandle.get<String>("id")!!
        item = getItemByIdUseCase(id)
    }
}