package com.rlad.core.infrastructure.rickandmorty.local

import androidx.room.TypeConverter

internal class RickAndMortyTypeConverters {

    @TypeConverter
    fun statusToInt(status: CharacterEntity.Status): Int = status.id

    @TypeConverter
    fun intToCharacterEntityStatus(value: Int): CharacterEntity.Status = CharacterEntity.Status.fromId(value)

    @TypeConverter
    fun genderToInt(gender: CharacterEntity.Gender): Int = gender.id

    @TypeConverter
    fun intToCharacterEntityGender(value: Int): CharacterEntity.Gender = CharacterEntity.Gender.fromId(value)
}
