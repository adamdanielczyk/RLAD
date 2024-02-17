package com.rlad.core.infrastructure.artic.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ArtworkEntity::class],
    version = 1,
)
internal abstract class ArticDatabase : RoomDatabase() {

    abstract fun artworkDao(): ArtworkDao
}
