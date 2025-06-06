package com.rlad.core.infrastructure.common.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AppPreferencesEntity::class,
    ],
    version = 1,
)
internal abstract class CommonDatabase : RoomDatabase() {

    abstract fun appPreferencesDao(): AppPreferencesDao
}
