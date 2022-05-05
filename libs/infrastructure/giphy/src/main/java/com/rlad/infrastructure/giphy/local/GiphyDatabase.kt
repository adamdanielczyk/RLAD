package com.rlad.infrastructure.giphy.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GifDataEntity::class],
    version = 1
)
internal abstract class GiphyDatabase : RoomDatabase() {

    abstract fun gifDataDao(): GifDataDao
}