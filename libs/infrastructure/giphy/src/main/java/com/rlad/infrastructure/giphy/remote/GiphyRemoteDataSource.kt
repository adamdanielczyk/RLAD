package com.rlad.infrastructure.giphy.remote

import com.rlad.infrastructure.giphy.BuildConfig
import javax.inject.Inject

internal class GiphyRemoteDataSource @Inject constructor(private val giphyApi: GiphyApi) {

    suspend fun trendingGifs(
        offset: Int,
        limit: Int,
    ): ServerGifs = giphyApi.trendingGifs(BuildConfig.GIPHY_API_KEY, offset, limit)

    suspend fun searchGifs(
        query: String,
        offset: Int,
        limit: Int,
    ): ServerGifs = giphyApi.searchGifs(BuildConfig.GIPHY_API_KEY, offset, limit, query)
}