package com.rlad.shared.data.rickandmorty.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class RickAndMortyApi(
    private val httpClient: HttpClient,
) {

    suspend fun getCharacters(
        page: Int,
        name: String?,
    ): ServerGetCharacters = httpClient.get("/api/character/") {
        parameter("page", page)
        if (name != null) {
            parameter("name", name)
        }
    }.body()

    suspend fun getCharacter(
        id: Int,
    ): ServerCharacter {
        return httpClient.get("/api/character/$id").body()
    }
}