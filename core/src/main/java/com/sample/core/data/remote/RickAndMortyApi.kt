package com.sample.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("/api/character/")
    suspend fun getAllCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?
    ): GetAllCharactersResponse
}