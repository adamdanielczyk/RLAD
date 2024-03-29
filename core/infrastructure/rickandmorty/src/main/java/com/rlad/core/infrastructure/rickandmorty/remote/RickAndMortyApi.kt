package com.rlad.core.infrastructure.rickandmorty.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RickAndMortyApi {

    @GET("/api/character/")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
    ): ServerGetCharacters

    @GET("/api/character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int,
    ): ServerCharacter
}