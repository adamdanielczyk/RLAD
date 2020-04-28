package com.sample.core.data.local

import androidx.room.TypeConverter

class Converters {

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