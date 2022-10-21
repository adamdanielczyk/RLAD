package com.rlad.core.infrastructure.giphy.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.giphy.BuildConfig
import javax.inject.Inject

internal class GiphyRemoteDataSource @Inject constructor(
    private val giphyApi: GiphyApi,
) : CommonRemoteDataSource<ServerGifs, ServerGifData> {

    override suspend fun getRootData(offset: Int): ServerGifs = giphyApi.trendingGifs(
        apiKey = BuildConfig.GIPHY_API_KEY,
        offset = offset,
        limit = getPageSize(),
    )

    override fun getItems(rootData: ServerGifs): List<ServerGifData> = rootData.data

    override suspend fun getItem(id: String): ServerGifData = giphyApi.getGif(
        gifId = id,
        apiKey = BuildConfig.GIPHY_API_KEY,
    ).data

    override fun getNextPagingOffset(rootData: ServerGifs, currentlyLoadedOffset: Int): Int {
        val pagination = rootData.pagination
        return pagination.offset + pagination.count
    }

    override fun getInitialPagingOffset(): Int = 0

    override fun getPageSize(): Int = 80

    suspend fun searchGifs(
        query: String,
        offset: Int,
        limit: Int,
    ): ServerGifs = giphyApi.searchGifs(
        apiKey = BuildConfig.GIPHY_API_KEY,
        offset = offset,
        limit = limit,
        query = query,
    )
}