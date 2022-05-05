package com.rlad.infrastructure.giphy.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rlad.infrastructure.giphy.remote.ServerGifData

@Entity(tableName = "gif_data")
internal data class GifDataEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val imageUrl: String,
) {

    constructor(serverGifData: ServerGifData) : this(
        id = serverGifData.id,
        title = serverGifData.title,
        imageUrl = serverGifData.images.fixedHeight.url,
    )
}