package com.rlad.core.infrastructure.rickandmorty.remote

import javax.inject.Inject

internal class RickAndMortyRemoteDataSource @Inject constructor(private val rickAndMortyApi: RickAndMortyApi) {

    suspend fun getCharacters(page: Int, name: String?): List<ServerCharacter> = rickAndMortyApi.getCharacters(page, name).results

    suspend fun getCharacter(id: Int): ServerCharacter = rickAndMortyApi.getCharacter(id)
}