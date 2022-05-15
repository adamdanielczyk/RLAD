package com.rlad.infrastructure.giphy.local

import androidx.paging.PagingSource
import com.rlad.infrastructure.giphy.local.GifDataEntity.OriginType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GiphyLocalDataSource @Inject constructor(private val gifDataDao: GifDataDao) {

    fun searchGifDataByTitle(title: String): PagingSource<Int, GifDataEntity> =
        gifDataDao.get(originType = OriginType.Search, title = title)

    fun getTrendingGifData(): PagingSource<Int, GifDataEntity> =
        gifDataDao.get(originType = OriginType.Trending, title = "")

    fun getGifDataById(giphyId: String): Flow<GifDataEntity> = gifDataDao.getById(giphyId)

    suspend fun insertGifsData(gifData: List<GifDataEntity>) {
        gifDataDao.insert(gifData)
    }

    suspend fun clearGifsData() {
        gifDataDao.clearTable()
    }
}