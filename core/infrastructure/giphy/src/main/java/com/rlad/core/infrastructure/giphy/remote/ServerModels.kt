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
    @Json(name = "slug") val slug: String,
    @Json(name = "username") val username: String,
    @Json(name = "source") val source: String,
    @Json(name = "title") val title: String,
    @Json(name = "rating") val rating: String,
    @Json(name = "content_url") val contentUrl: String,
    @Json(name = "source_tld") val sourceTld: String,
    @Json(name = "source_post_url") val sourcePostUrl: String,
    @Json(name = "import_datetime") val importDatetime: String,
    @Json(name = "trending_datetime") val trendingDatetime: String,
    @Json(name = "images") val images: Images,
) {

    @JsonClass(generateAdapter = true)
    data class Images(
        @Json(name = "original") val original: Image,
        @Json(name = "fixed_height") val fixedHeight: Image,
        @Json(name = "fixed_width") val fixedWidth: Image,
    ) {

        @JsonClass(generateAdapter = true)
        data class Image(
            @Json(name = "height") val height: String,
            @Json(name = "width") val width: String,
            @Json(name = "size") val size: String,
            @Json(name = "url") val url: String,
        )
    }
}

