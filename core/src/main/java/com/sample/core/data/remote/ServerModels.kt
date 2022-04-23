package com.sample.core.data.remote

import com.squareup.moshi.Json

data class GetCharactersResponse(
    @Json(name = "results") val results: List<ServerCharacter>,
)

data class ServerCharacter(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: Status,
    @Json(name = "species") val species: String,
    @Json(name = "type") val type: String,
    @Json(name = "gender") val gender: Gender,
    @Json(name = "location") val location: Location,
    @Json(name = "image") val imageUrl: String,
    @Json(name = "created") val created: String,
) {

    enum class Status {
        @Json(name = "Alive")
        ALIVE,

        @Json(name = "Dead")
        DEAD,

        @Json(name = "unknown")
        UNKNOWN
    }

    enum class Gender {
        @Json(name = "Female")
        FEMALE,

        @Json(name = "Male")
        MALE,

        @Json(name = "Genderless")
        GENDERLESS,

        @Json(name = "unknown")
        UNKNOWN
    }

    data class Location(
        @Json(name = "name") val name: String,
    )
}