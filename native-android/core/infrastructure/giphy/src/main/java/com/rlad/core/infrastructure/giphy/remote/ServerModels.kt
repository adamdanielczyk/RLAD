package com.rlad.core.infrastructure.giphy.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerGifRoot(
    @SerialName("data") val data: ServerGif,
)

@Serializable
data class ServerGifsRoot(
    @SerialName("data") val data: List<ServerGif>,
    @SerialName("pagination") val pagination: ServerPagination,
)

@Serializable
data class ServerPagination(
    @SerialName("offset") val offset: Int,
    @SerialName("count") val count: Int,
)

@Serializable
data class ServerGif(
    @SerialName("id") val id: String,
    @SerialName("url") val url: String,
    @SerialName("username") val username: String,
    @SerialName("title") val title: String,
    @SerialName("import_datetime") val importDatetime: String,
    @SerialName("trending_datetime") val trendingDatetime: String,
    @SerialName("images") val images: Images,
) {

    @Serializable
    data class Images(
        @SerialName("fixed_width") val fixedWidth: Image,
    ) {

        @Serializable
        data class Image(
            @SerialName("url") val url: String,
        )
    }
}
