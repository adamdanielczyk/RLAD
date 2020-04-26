package com.sample.data.remote

import com.squareup.moshi.Json

data class GetAllCharactersResponse(
    @Json(name = "info") val info: ServerInfo,
    @Json(name = "results") val results: List<ServerCharacter>
)

data class ServerInfo(
    @Json(name = "count") val count: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "next") val next: String,
    @Json(name = "prev") val prev: String
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
    @Json(name = "created") val created: String
) {

    enum class Status {
        @Json(name = "Alive") ALIVE,
        @Json(name = "Dead") DEAD,
        @Json(name = "unknown") UNKNOWN
    }

    enum class Gender {
        @Json(name = "Female") FEMALE,
        @Json(name = "Male") MALE,
        @Json(name = "Genderless") GENDERLESS,
        @Json(name = "unknown") UNKNOWN
    }

    data class Location(
        val name: String,
        val url: String?
    )
}