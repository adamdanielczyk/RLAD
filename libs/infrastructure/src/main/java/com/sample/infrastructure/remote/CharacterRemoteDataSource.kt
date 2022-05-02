package com.sample.infrastructure.remote

import javax.inject.Inject

internal class CharacterRemoteDataSource @Inject constructor(private val rickAndMortyApi: RickAndMortyApi) {

    suspend fun getCharacters(page: Int, name: String?): List<ServerCharacter> =
        rickAndMortyApi.getCharacters(page, name).results
}