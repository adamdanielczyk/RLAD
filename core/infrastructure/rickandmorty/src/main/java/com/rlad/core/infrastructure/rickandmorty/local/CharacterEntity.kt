package com.rlad.core.infrastructure.rickandmorty.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character")
internal data class CharacterEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "status") val status: Status,
    @ColumnInfo(name = "species") val species: String,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "gender") val gender: Gender,
    @Embedded(prefix = "location_") val location: Location,
    @ColumnInfo(name = "image") val imageUrl: String,
    @ColumnInfo(name = "created") val created: String,
) {

    enum class Status(val id: Int) {
        ALIVE(1),
        DEAD(2),
        UNKNOWN(3);

        companion object {
            fun fromId(id: Int): Status = values().first { it.id == id }
        }
    }

    enum class Gender(val id: Int) {
        FEMALE(1),
        MALE(2),
        GENDERLESS(3),
        UNKNOWN(4);

        companion object {
            fun fromId(id: Int): Gender = values().first { it.id == id }
        }
    }

    data class Location(
        @ColumnInfo(name = "name") val name: String,
    )
}