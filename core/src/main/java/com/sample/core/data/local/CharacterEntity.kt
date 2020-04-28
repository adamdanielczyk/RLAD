package com.sample.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.core.data.remote.ServerCharacter

@Entity(tableName = "character")
data class CharacterEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "status") val status: Status,
    @ColumnInfo(name = "species") val species: String,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "gender") val gender: Gender,
    @Embedded(prefix = "location") val location: Location,
    @ColumnInfo(name = "image") val imageUrl: String,
    @ColumnInfo(name = "created") val created: String
) {

    constructor(serverCharacter: ServerCharacter) : this(
        id = serverCharacter.id,
        name = serverCharacter.name,
        status = Status.fromServerStatus(
            serverCharacter.status
        ),
        species = serverCharacter.species,
        type = serverCharacter.type,
        gender = Gender.fromServerStatus(
            serverCharacter.gender
        ),
        location = Location(serverCharacter.location),
        imageUrl = serverCharacter.imageUrl,
        created = serverCharacter.created
    )

    enum class Status(val id: Int) {
        ALIVE(1),
        DEAD(2),
        UNKNOWN(3);

        companion object {
            fun fromId(id: Int): Status = values().first { it.id == id }

            fun fromServerStatus(serverStatus: ServerCharacter.Status): Status =
                when (serverStatus) {
                    ServerCharacter.Status.ALIVE -> ALIVE
                    ServerCharacter.Status.DEAD -> DEAD
                    ServerCharacter.Status.UNKNOWN -> UNKNOWN
                }
        }
    }

    enum class Gender(val id: Int) {
        FEMALE(1),
        MALE(2),
        GENDERLESS(3),
        UNKNOWN(4);

        companion object {
            fun fromId(id: Int): Gender = values().first { it.id == id }

            fun fromServerStatus(serverGender: ServerCharacter.Gender): Gender =
                when (serverGender) {
                    ServerCharacter.Gender.FEMALE -> FEMALE
                    ServerCharacter.Gender.MALE -> MALE
                    ServerCharacter.Gender.GENDERLESS -> GENDERLESS
                    ServerCharacter.Gender.UNKNOWN -> UNKNOWN
                }
        }
    }

    data class Location(
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "url") val url: String?
    ) {

        constructor(serverLocation: ServerCharacter.Location) : this(
            name = serverLocation.name,
            url = serverLocation.url
        )
    }
}