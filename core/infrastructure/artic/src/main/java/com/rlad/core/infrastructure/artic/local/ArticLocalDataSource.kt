package com.rlad.core.infrastructure.artic.local

import androidx.paging.PagingSource
import com.rlad.core.infrastructure.common.local.CommonLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ArticLocalDataSource @Inject constructor(
    private val artworkDataDao: ArtworkDataDao,
) : CommonLocalDataSource<ArtworkDataEntity> {

    override suspend fun insert(data: List<ArtworkDataEntity>) {
        artworkDataDao.insert(data)
    }

    override suspend fun clear() {
        artworkDataDao.clearTable()
    }

    override fun getAll(): PagingSource<Int, ArtworkDataEntity> = artworkDataDao.getAll()

    override fun getById(id: String): Flow<ArtworkDataEntity?> = artworkDataDao.getById(id.toInt())
}