package com.rlad.shared.data.giphy.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class GiphyApi(
    private val httpClient: HttpClient,
    private val apiKey: String,
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
        parameter("api_key", apiKey)
    }

    private fun HttpRequestBuilder.bundleParameter() {
        parameter("bundle", "clips_grid_picker")
    }
}