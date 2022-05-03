package com.sample.infrastructure.giphy.remote

import com.sample.infrastructure.giphy.BuildConfig
import javax.inject.Inject

internal class GiphyRemoteDataSource @Inject constructor(private val giphyApi: GiphyApi) {

    suspend fun getGifsData(
        offset: Int,
        limit: Int,
    ): List<ServerGifData> = giphyApi.getTrendingGifs(BuildConfig.GIPHY_API_KEY, offset, limit).data
}