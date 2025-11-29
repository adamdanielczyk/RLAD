package com.rlad.core.infrastructure.giphy.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GifEntity::class],
    version = 1,
)
abstract class GiphyDatabase : RoomDatabase() {

    abstract fun gifDao(): GifDao
}
