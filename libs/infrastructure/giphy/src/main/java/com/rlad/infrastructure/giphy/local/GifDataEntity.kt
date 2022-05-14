package com.rlad.infrastructure.giphy.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rlad.infrastructure.giphy.remote.ServerGifData

@Entity(tableName = "gif_data")
internal data class GifDataEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "origin_type") val originType: OriginType,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val imageUrl: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "import_datetime") val importDatetime: String,
    @ColumnInfo(name = "trending_datetime") val trendingDatetime: String,
) {

    constructor(serverGifData: ServerGifData, originType: OriginType) : this(
        originType = originType,
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