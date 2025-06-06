package com.rlad.core.infrastructure.common.repository

import androidx.paging.PagingData
import com.rlad.core.domain.model.ItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun createCommonRepository(items: List<ItemUiModel> = emptyList()): CommonRepository = object : CommonRepository {
    override fun getItemById(id: String): Flow<ItemUiModel> =
        flowOf(items.first { it.id == id })

    override fun getAllItems(): Flow<PagingData<ItemUiModel>> =
        flowOf(PagingData.from(items))

    override fun getSearchItems(query: String): Flow<PagingData<ItemUiModel>> =
        flowOf(PagingData.from(items.filter { it.name == query }))
}