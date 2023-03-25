package com.rlad.core.infrastructure.giphy.local

import androidx.paging.PagingSource
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GiphyLocalDataSource @Inject constructor(
    private val gifDao: GifDao,
) : CommonLocalDataSource<GifEntity> {

    override suspend fun insert(data: List<GifEntity>) {
        gifDao.insert(data)
    }

    override suspend fun clear() {
        gifDao.clearTable()
    }

    override fun getAll(): PagingSource<Int, GifEntity> = gifDao.getAll()

    override fun getById(id: String): Flow<GifEntity?> = gifDao.getById(id)
}