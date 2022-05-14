package com.rlad.infrastructure.giphy.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rlad.infrastructure.giphy.remote.ServerGifData

@Entity(tableName = "gif_data")
internal data class GifDataEntity(
    @PrimaryKey @ColumnInfo(name = "giphy_id") val giphyId: String,
    @ColumnInfo(name = "order_id") val orderId: Int,
    @ColumnInfo(name = "origin_type") val originType: OriginType,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val imageUrl: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "import_datetime") val importDatetime: String,
    @ColumnInfo(name = "trending_datetime") val trendingDatetime: String,
) {

    constructor(
        serverGifData: ServerGifData,
        originType: OriginType,
        orderId: Int,
    ) : this(
        orderId = orderId,
        originType = originType,
        giphyId = serverGifData.id,
        title = serverGifData.title,
        imageUrl = serverGifData.images.fixedHeight.url,
        username = serverGifData.username,
        importDatetime = serverGifData.importDatetime,
        trendingDatetime = serverGifData.trendingDatetime,
    )

    enum class OriginType(val id: String) {
        Search("search"),
        Trending("trending");

        companion object {
            fun fromId(id: String): OriginType = values().first { it.id == id }
        }
    }
}