package com.rlad.core.infrastructure.giphy.remote

import com.rlad.core.infrastructure.giphy.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal class GiphyApi @Inject constructor(
    private val httpClient: HttpClient,
) {

    suspend fun trendingGifs(
        offset: Int,
        limit: Int,
    ): ServerGifsRoot = httpClient.get("/v1/gifs/trending") {
        apiKeyParameter()
        bundleParameter()
        parameter("offset", offset)
        parameter("limit", limit)
    }.body()

    suspend fun searchGifs(
        offset: Int,
        limit: Int,
        query: String,
    ): ServerGifsRoot = httpClient.get("/v1/gifs/search") {
        apiKeyParameter()
        bundleParameter()
        parameter("offset", offset)
        parameter("limit", limit)
        parameter("q", query)
    }.body()

    suspend fun getGif(
        gifId: String,
    ): ServerGifRoot = httpClient.get("/v1/gifs/$gifId") {
        apiKeyParameter()
    }.body()

    private fun HttpRequestBuilder.apiKeyParameter() {
        parameter("api_key", BuildConfig.GIPHY_API_KEY)
    }

    private fun HttpRequestBuilder.bundleParameter() {
        parameter("bundle", "clips_grid_picker")
    }
}
