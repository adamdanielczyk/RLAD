package com.rlad.core.infrastructure.giphy.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rlad.core.infrastructure.giphy.remote.ServerGifData

@Entity(
    tableName = "gif_data",
    indices = [
        Index(value = ["giphy_id"], unique = true)
    ],
)
internal data class GifDataEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "giphy_id") val giphyId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val imageUrl: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "import_datetime") val importDatetime: String,
    @ColumnInfo(name = "trending_datetime") val trendingDatetime: String,
) {

    constructor(serverGifData: ServerGifData) : this(
        giphyId = serverGifData.id,
        title = serverGifData.title,
        imageUrl = serverGifData.images.fixedHeight.url,
        username = serverGifData.username,
        importDatetime = serverGifData.importDatetime,
        trendingDatetime = serverGifData.trendingDatetime,
    )
}