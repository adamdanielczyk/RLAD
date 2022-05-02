package com.sample.infrastructure.rickandmorty.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CharacterEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
internal abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}