package com.sample.infrastructure.common.repository

import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.sample.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getDataSourceName(): String

    @StringRes
    fun getDataSourcePickerTextResId(): Int

    fun getItemById(id: String): Flow<ItemUiModel>

    fun getItems(query: String?): Flow<PagingData<ItemUiModel>>
}