package com.rlad.core.infrastructure.giphy.local

import androidx.paging.PagingSource
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GiphyLocalDataSource @Inject constructor(
    private val gifDataDao: GifDataDao,
) : CommonLocalDataSource<GifDataEntity> {

    override suspend fun insert(data: List<GifDataEntity>) {
        gifDataDao.insert(data)
    }

    override suspend fun clear() {
        gifDataDao.clearTable()
    }

    override fun getAll(): PagingSource<Int, GifDataEntity> = gifDataDao.getAll()

    override fun getById(id: String): Flow<GifDataEntity?> = gifDataDao.getById(id)
}