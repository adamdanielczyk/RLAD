package com.sample.infrastructure.giphy.remote

import retrofit2.http.GET
import retrofit2.http.Query

internal interface GiphyApi {

    @GET("/v1/gifs/trending")
    suspend fun trendingGifs(
        @Query("api_key") apiKey: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): ServerGifs

    @GET("/v1/gifs/search")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("q") query: String,
    ): ServerGifs
}