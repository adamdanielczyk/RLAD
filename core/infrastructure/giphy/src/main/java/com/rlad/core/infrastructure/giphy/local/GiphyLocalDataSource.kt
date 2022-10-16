package com.rlad.core.infrastructure.giphy.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GiphyLocalDataSource @Inject constructor(private val gifDataDao: GifDataDao) {

    fun getAllGifData(): PagingSource<Int, GifDataEntity> = gifDataDao.getAll()

    fun getGifDataById(giphyId: String): Flow<GifDataEntity?> = gifDataDao.getById(giphyId)

    suspend fun insertGifsData(gifData: List<GifDataEntity>) {
        gifDataDao.insert(gifData)
    }

    suspend fun clearGifsData() {
        gifDataDao.clearTable()
    }
}