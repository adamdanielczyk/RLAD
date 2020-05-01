package com.sample.core.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRemoteDataSource @Inject constructor(private val rickAndMortyApi: RickAndMortyApi) {

    suspend fun getCharacters(page: Int, name: String?): List<ServerCharacter> =
        rickAndMortyApi.getCharacters(page, name).results
}