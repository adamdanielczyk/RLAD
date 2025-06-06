package com.rlad.core.infrastructure.giphy.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ServerGifRoot(
    @Json(name = "data") val data: ServerGif,
)

@JsonClass(generateAdapter = true)
internal data class ServerGifsRoot(
    @Json(name = "data") val data: List<ServerGif>,
    @Json(name = "pagination") val pagination: ServerPagination,
)

@JsonClass(generateAdapter = true)
internal data class ServerPagination(
    @Json(name = "offset") val offset: Int,
    @Json(name = "count") val count: Int,
)

@JsonClass(generateAdapter = true)
internal data class ServerGif(
    @Json(name = "id") val id: String,
    @Json(name = "url") val url: String,
    @Json(name = "username") val username: String,
    @Json(name = "title") val title: String,
    @Json(name = "import_datetime") val importDatetime: String,
    @Json(name = "trending_datetime") val trendingDatetime: String,
    @Json(name = "images") val images: Images,
) {

    @JsonClass(generateAdapter = true)
    data class Images(
        @Json(name = "fixed_width") val fixedWidth: Image,
    ) {

        @JsonClass(generateAdapter = true)
        data class Image(
            @Json(name = "url") val url: String,
        )
    }
}

