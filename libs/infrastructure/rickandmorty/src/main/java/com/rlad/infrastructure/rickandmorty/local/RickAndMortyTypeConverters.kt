package com.rlad.infrastructure.rickandmorty.local

import androidx.room.TypeConverter

internal class RickAndMortyTypeConverters {

    @TypeConverter
    fun CharacterEntity.Status.toInt() = id

    @TypeConverter
    fun Int.toCharacterEntityStatus() =
        CharacterEntity.Status.fromId(this)

    @TypeConverter
    fun CharacterEntity.Gender.toInt() = id

    @TypeConverter
    fun Int.toCharacterEntityGender() =
        CharacterEntity.Gender.fromId(this)
}