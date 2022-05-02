package com.sample.features.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val repository: ItemsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    fun getItem(): Flow<ItemUiModel> {
        val id = savedStateHandle.get<String>("id")!!.toInt()
        return repository.getItemBy(id)
    }
}