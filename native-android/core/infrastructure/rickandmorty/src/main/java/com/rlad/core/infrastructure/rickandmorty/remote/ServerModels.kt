package com.rlad.core.infrastructure.rickandmorty.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ServerGetCharacters(
    @SerialName("results") val results: List<ServerCharacter> = emptyList(),
    @SerialName("error") val error: String? = null,
)

@Serializable
internal data class ServerCharacter(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("status") val status: Status,
    @SerialName("species") val species: String,
    @SerialName("type") val type: String,
    @SerialName("gender") val gender: Gender,
    @SerialName("location") val location: Location,
    @SerialName("image") val imageUrl: String,
    @SerialName("created") val created: String,
) {

    @Serializable
    enum class Status {
        @SerialName("Alive")
        ALIVE,

        @SerialName("Dead")
        DEAD,

        @SerialName("unknown")
        UNKNOWN
    }

    @Serializable
    enum class Gender {
        @SerialName("Female")
        FEMALE,

        @SerialName("Male")
        MALE,

        @SerialName("Genderless")
        GENDERLESS,

        @SerialName("unknown")
        UNKNOWN
    }

    @Serializable
    data class Location(
        @SerialName("name") val name: String,
    )
}