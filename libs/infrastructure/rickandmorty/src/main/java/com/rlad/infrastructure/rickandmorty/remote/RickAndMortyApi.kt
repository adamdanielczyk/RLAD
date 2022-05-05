package com.rlad.infrastructure.rickandmorty.remote

import retrofit2.http.GET
import retrofit2.http.Query

internal interface RickAndMortyApi {

    @GET("/api/character/")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
    ): ServerGetCharacters
}