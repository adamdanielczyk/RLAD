package com.rlad.core.infrastructure.artic.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ArticApi {

    @GET("/api/v1/artworks/search?$FIELDS&$ELASTICSEARCH_QUERY_ONLY_WITH_IMAGE_IDS")
    suspend fun artworks(
        @Query("from") from: Int,
        @Query("size") size: Int,
    ): ServerArtworks

    @GET("/api/v1/artworks/{id}?$FIELDS")
    suspend fun artwork(
        @Path("id") id: String,
    ): ServerArtwork

    @GET("/api/v1/artworks/search?$FIELDS&$ELASTICSEARCH_QUERY_ONLY_WITH_IMAGE_IDS")
    suspend fun search(
        @Query("q") query: String,
        @Query("from") from: Int,
        @Query("size") size: Int,
    ): ServerArtworks

    private companion object {
        const val FIELDS = "fields=id,title,image_id,artist_title,artist_display,department_title,place_of_origin,date_display,thumbnail"
        const val ELASTICSEARCH_QUERY_ONLY_WITH_IMAGE_IDS = "query[exists][field]=image_id"
    }
}