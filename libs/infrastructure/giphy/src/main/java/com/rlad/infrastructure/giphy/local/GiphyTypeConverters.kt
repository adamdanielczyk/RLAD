package com.rlad.infrastructure.giphy.local

import androidx.room.TypeConverter

internal class GiphyTypeConverters {

    @TypeConverter
    fun GifDataEntity.OriginType.toId() = id

    @TypeConverter
    fun String.toOriginType() = GifDataEntity.OriginType.fromId(this)
}