package com.rlad.core.infrastructure.rickandmorty.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CharacterEntity::class],
    version = 1,
)
@TypeConverters(RickAndMortyTypeConverters::class)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}
