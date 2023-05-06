package com.rlad.core.infrastructure.giphy.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GiphyApi {

    @GET("/v1/gifs/trending?$BUNDLE")
    suspend fun trendingGifs(
        @Query("api_key") apiKey: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): ServerGifsRoot

    @GET("/v1/gifs/search?$BUNDLE")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("q") query: String,
    ): ServerGifsRoot

    @GET("/v1/gifs/{gif_id}")
    suspend fun getGif(
        @Path("gif_id") gifId: String,
        @Query("api_key") apiKey: String,
    ): ServerGifRoot

    private companion object {
        const val BUNDLE = "bundle=clips_grid_picker"
    }
}