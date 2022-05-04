package com.sample.infrastructure.giphy.remote

import com.squareup.moshi.Json

internal data class ServerTrendingGifs(
    @Json(name = "data") val data: List<ServerGifData>,
)

internal data class ServerGifData(
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
)

internal data class Images(
    @Json(name = "original") val original: Image,
    @Json(name = "downsized") val downsized: Image,
    @Json(name = "downsized_large") val downsizedLarge: Image,
    @Json(name = "downsized_medium") val downsizedMedium: Image,
    @Json(name = "downsized_still") val downsizedStill: Image,
    @Json(name = "fixed_height") val fixedHeight: Image,
    @Json(name = "fixed_height_downsampled") val fixedHeightDownsampled: Image,
    @Json(name = "fixed_height_small") val fixedHeightSmall: Image,
    @Json(name = "fixed_width") val fixedWidth: Image,
    @Json(name = "fixed_width_downsampled") val fixedWidthDownsampled: Image,
    @Json(name = "fixed_width_small") val fixedWidthSmall: Image,
)

internal data class Image(
    @Json(name = "height") val height: String,
    @Json(name = "width") val width: String,
    @Json(name = "size") val size: String,
    @Json(name = "url") val url: String,
)