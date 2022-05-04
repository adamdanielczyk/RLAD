package com.sample.domain.repository

import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.sample.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getDataSourceName(): String

    @StringRes
    fun getDataSourcePickerTextResId(): Int

    fun getItemById(id: String): Flow<ItemUiModel>

    fun getItems(name: String?): Flow<PagingData<ItemUiModel>>
}