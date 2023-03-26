package com.rlad.core.infrastructure.giphy.remote

import com.rlad.core.infrastructure.common.remote.CommonRemoteDataSource
import com.rlad.core.infrastructure.giphy.BuildConfig
import javax.inject.Inject

internal class GiphyRemoteDataSource @Inject constructor(
    private val giphyApi: GiphyApi,
) : CommonRemoteDataSource<ServerGifsRoot, ServerGif> {

    override suspend fun getRootData(offset: Int): ServerGifsRoot = giphyApi.trendingGifs(
        apiKey = BuildConfig.GIPHY_API_KEY,
        offset = offset,
        limit = getPageSize(),
    )

    override fun getItems(rootData: ServerGifsRoot): List<ServerGif> = rootData.data

    override suspend fun getItem(id: String): ServerGif = giphyApi.getGif(
        gifId = id,
        apiKey = BuildConfig.GIPHY_API_KEY,
    ).data

    override fun getNextPagingOffset(rootData: ServerGifsRoot, currentlyLoadedPage: Int): Int {
        val pagination = rootData.pagination
        return pagination.offset + pagination.count
    }

    override fun getInitialPagingOffset(): Int = 0

    override fun getPageSize(): Int = 80

    override suspend fun search(query: String, offset: Int): ServerGifsRoot = giphyApi.searchGifs(
        apiKey = BuildConfig.GIPHY_API_KEY,
        offset = offset,
        limit = getPageSize(),
        query = query,
    )
}