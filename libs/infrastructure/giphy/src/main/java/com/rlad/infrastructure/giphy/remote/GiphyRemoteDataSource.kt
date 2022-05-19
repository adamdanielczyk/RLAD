package com.rlad.infrastructure.giphy.remote

import com.rlad.infrastructure.giphy.BuildConfig
import javax.inject.Inject

internal class GiphyRemoteDataSource @Inject constructor(private val giphyApi: GiphyApi) {

    suspend fun trendingGifs(
        offset: Int,
        limit: Int,
    ): ServerGifs = giphyApi.trendingGifs(
        apiKey = BuildConfig.GIPHY_API_KEY,
        offset = offset,
        limit = limit,
    )

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

    suspend fun getGif(
        gifId: String,
    ): ServerGif = giphyApi.getGif(
        gifId = gifId,
        apiKey = BuildConfig.GIPHY_API_KEY,
    )
}