package com.rlad.core.infrastructure.giphy.local

import androidx.paging.PagingSource
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
@ContributesBinding(AppScope::class)
class GiphyLocalDataSource(
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
