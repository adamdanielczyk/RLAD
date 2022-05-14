package com.rlad.infrastructure.giphy.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rlad.infrastructure.giphy.remote.ServerGifData

@Entity(tableName = "gif_data")
internal data class GifDataEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val imageUrl: String,
    @ColumnInfo(name = "origin_type") val originType: OriginType,
) {

    constructor(serverGifData: ServerGifData, originType: OriginType) : this(
        title = serverGifData.title,
        imageUrl = serverGifData.images.fixedHeight.url,
        originType = originType,
    )

    enum class OriginType(val id: String) {
        Search("search"),
        Trending("trending");

        companion object {
            fun fromId(id: String): OriginType = values().first { it.id == id }
        }
    }
}