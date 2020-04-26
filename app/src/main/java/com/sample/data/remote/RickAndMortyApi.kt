package com.sample.data.remote

import retrofit2.http.GET

interface RickAndMortyApi {

    @GET("/api/character/")
    suspend fun getAllCharacters(): GetAllCharactersResponse
}