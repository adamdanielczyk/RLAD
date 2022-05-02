package com.sample.infrastructure.local

import androidx.room.TypeConverter

internal class Converters {

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