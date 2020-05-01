package com.sample.core.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRickAndMortyApi @Inject constructor() : RickAndMortyApi {

    private val characters = mutableListOf<ServerCharacter>()

    fun addCharacters(characters: List<ServerCharacter>) {
        this.characters.addAll(characters)
    }

    override suspend fun getCharacters(page: Int, name: String?): GetCharactersResponse {
        val characters = if (name != null) {
            characters.filter { it.name.contains(name) }
        } else {
            characters
        }
        return GetCharactersResponse(characters)
    }
}