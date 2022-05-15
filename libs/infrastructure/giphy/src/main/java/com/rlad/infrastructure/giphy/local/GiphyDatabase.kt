package com.rlad.infrastructure.giphy.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        GifDataEntity::class,
        GiphyPreferencesEntity::class,
    ],
    version = 1
)
@TypeConverters(GiphyTypeConverters::class)
internal abstract class GiphyDatabase : RoomDatabase() {

    abstract fun gifDataDao(): GifDataDao
    abstract fun giphyPreferencesDao(): GiphyPreferencesDao
}