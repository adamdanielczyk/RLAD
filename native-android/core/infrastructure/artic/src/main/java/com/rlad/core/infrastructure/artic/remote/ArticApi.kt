package com.rlad.core.infrastructure.artic.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal class ArticApi @Inject constructor(
    private val httpClient: HttpClient,
) {

    suspend fun artworks(
        query: String?,
        from: Int,
        size: Int,
    ): ServerArtworksRoot = httpClient.get("/api/v1/artworks/search") {
        fieldsParameter()
        parameter("query[exists][field]", "image_id")
        parameter("from", from)
        parameter("size", size)
        if (query != null) {
            parameter("q", query)
        }
    }.body()

    suspend fun artwork(
        id: String,
    ): ServerArtworkRoot = httpClient.get("/api/v1/artworks/$id") {
        fieldsParameter()
    }.body()

    private fun HttpRequestBuilder.fieldsParameter() {
        parameter("fields", "id,title,image_id,artist_title,artist_display,department_title,place_of_origin,date_display,thumbnail")
    }
}
