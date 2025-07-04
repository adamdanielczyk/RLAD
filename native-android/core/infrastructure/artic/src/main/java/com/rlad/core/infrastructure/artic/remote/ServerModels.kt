package com.rlad.core.infrastructure.artic.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ServerArtworkRoot(
    @SerialName("data") val data: ServerArtwork,
)

@Serializable
internal data class ServerArtworksRoot(
    @SerialName("pagination") val pagination: ServerPagination,
    @SerialName("data") val data: List<ServerArtwork>,
) {

    @Serializable
    data class ServerPagination(
        @SerialName("limit") val limit: Int,
        @SerialName("offset") val offset: Int,
    )
}

@Serializable
internal data class ServerArtwork(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("image_id") val imageId: String,
    @SerialName("thumbnail") val thumbnail: Thumbnail?,
    @SerialName("artist_title") val artistTitle: String?,
    @SerialName("artist_display") val artistDisplay: String?,
    @SerialName("place_of_origin") val placeOfOrigin: String?,
    @SerialName("department_title") val departmentTitle: String?,
    @SerialName("date_display") val dateDisplay: String?,
) {

    @Serializable
    data class Thumbnail(
        @SerialName("alt_text") val altText: String?,
    )
}
