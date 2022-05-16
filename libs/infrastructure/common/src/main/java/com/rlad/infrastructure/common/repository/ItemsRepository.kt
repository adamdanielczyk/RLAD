package com.rlad.infrastructure.common.repository

import androidx.paging.PagingData
import com.rlad.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getDataSourceName(): String

    fun getDataSourcePickerText(): String

    fun getItemById(id: String): Flow<ItemUiModel>

    fun getAllItems(): Flow<PagingData<ItemUiModel>>

    fun getSearchItems(query: String): Flow<PagingData<ItemUiModel>>
}