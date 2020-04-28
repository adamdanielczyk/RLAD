package com.sample.core.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRemoteDataSource @Inject constructor(private val rickAndMortyApi: RickAndMortyApi) {

    suspend fun getAllCharacters(): GetAllCharactersResponse = rickAndMortyApi.getAllCharacters()
}