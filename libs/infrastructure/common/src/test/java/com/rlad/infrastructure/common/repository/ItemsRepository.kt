package com.rlad.infrastructure.common.repository

import androidx.paging.PagingData
import com.rlad.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun createItemsRepository(
    dataSourceName: String,
    pickerText: String,
    items: List<ItemUiModel> = emptyList(),
): ItemsRepository = object : ItemsRepository {
    override fun getDataSourceName(): String = dataSourceName

    override fun getDataSourcePickerText(): String = pickerText

    override fun getItemById(id: String): Flow<ItemUiModel> =
        flowOf(items.first { it.id == id })

    override fun getAllItems(): Flow<PagingData<ItemUiModel>> =
        flowOf(PagingData.from(items))

    override fun getSearchItems(query: String): Flow<PagingData<ItemUiModel>> =
        flowOf(PagingData.from(items.filter { it.name == query }))
}