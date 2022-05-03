package com.sample.domain.repository

import androidx.paging.PagingData
import com.sample.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getItemBy(id: String): Flow<ItemUiModel>

    fun getItems(name: String? = null): Flow<PagingData<ItemUiModel>>
}