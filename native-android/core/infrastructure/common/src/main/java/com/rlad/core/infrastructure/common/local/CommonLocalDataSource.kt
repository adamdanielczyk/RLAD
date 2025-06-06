package com.rlad.core.infrastructure.common.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

interface CommonLocalDataSource<LocalModel : Any> {

    suspend fun insert(data: List<LocalModel>)
    suspend fun clear()
    fun getAll(): PagingSource<Int, LocalModel>
    fun getById(id: String): Flow<LocalModel?>
}