package com.sample.infrastructure.giphy.local

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GiphyLocalDataSource @Inject constructor(private val gifDataDao: GifDataDao) {

    fun getGifDataByTitle(title: String): PagingSource<Int, GifDataEntity> =
        gifDataDao.getByTitle(title)

    fun getGifDataById(id: String): Flow<GifDataEntity> = gifDataDao.getById(id)

    suspend fun insertGifsData(gifData: List<GifDataEntity>) {
        gifDataDao.insert(gifData)
    }
}