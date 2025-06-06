package com.rlad.core.infrastructure.artic.local

import androidx.paging.PagingSource
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ArticLocalDataSource @Inject constructor(
    private val artworkDao: ArtworkDao,
) : CommonLocalDataSource<ArtworkEntity> {

    override suspend fun insert(data: List<ArtworkEntity>) {
        artworkDao.insert(data)
    }

    override suspend fun clear() {
        artworkDao.clearTable()
    }

    override fun getAll(): PagingSource<Int, ArtworkEntity> = artworkDao.getAll()

    override fun getById(id: String): Flow<ArtworkEntity?> = artworkDao.getById(id.toInt())
}