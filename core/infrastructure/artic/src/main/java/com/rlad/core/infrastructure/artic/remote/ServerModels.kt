package com.rlad.core.infrastructure.artic.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ServerArtworkRoot(
    @Json(name = "data") val data: ServerArtwork,
)

@JsonClass(generateAdapter = true)
internal data class ServerArtworksRoot(
    @Json(name = "pagination") val pagination: ServerPagination,
    @Json(name = "data") val data: List<ServerArtwork>,
) {

    @JsonClass(generateAdapter = true)
    data class ServerPagination(
        @Json(name = "limit") val limit: Int,
        @Json(name = "offset") val offset: Int,
    )
}

@JsonClass(generateAdapter = true)
internal data class ServerArtwork(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "image_id") val imageId: String,
    @Json(name = "thumbnail") val thumbnail: Thumbnail?,
    @Json(name = "artist_title") val artistTitle: String?,
    @Json(name = "artist_display") val artistDisplay: String?,
    @Json(name = "place_of_origin") val placeOfOrigin: String?,
    @Json(name = "department_title") val departmentTitle: String?,
    @Json(name = "date_display") val dateDisplay: String?,
) {

    @JsonClass(generateAdapter = true)
    data class Thumbnail(
        @Json(name = "alt_text") val altText: String?,
    )
}
