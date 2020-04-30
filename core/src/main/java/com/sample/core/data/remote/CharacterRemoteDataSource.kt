package com.sample.core.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRemoteDataSource @Inject constructor(private val rickAndMortyApi: RickAndMortyApi) {

    suspend fun getAllCharacters(page: Int, name: String?): GetAllCharactersResponse =
        rickAndMortyApi.getAllCharacters(page, name)
}