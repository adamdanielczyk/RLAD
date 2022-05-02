package com.sample.infrastructure.remote

import com.sample.infrastructure.remote.ServerCharacter.Gender
import com.sample.infrastructure.remote.ServerCharacter.Location
import com.sample.infrastructure.remote.ServerCharacter.Status
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterRemoteDataSourceTest {

    private val characters = listOf(
        ServerCharacter(
            id = 1,
            name = "Rick Sanchez",
            status = Status.ALIVE,
            species = "species 1",
            type = "type 1",
            gender = Gender.MALE,
            location = Location("location 1"),
            imageUrl = "url 1",
            created = "created 1"
        ),
        ServerCharacter(
            id = 2,
            name = "Morty Smith",
            status = Status.ALIVE,
            species = "species 2",
            type = "type 2",
            gender = Gender.MALE,
            location = Location("location 2"),
            imageUrl = "url 2",
            created = "created 2"
        )
    )

    private val fakeRickAndMortyApi = FakeRickAndMortyApi(characters)
    private val characterRemoteDataSource = CharacterRemoteDataSource(fakeRickAndMortyApi)

    @Test
    fun getCharacters_allApiItemsAreReturned() = runTest {
        assertEquals(
            characters,
            characterRemoteDataSource.getCharacters(page = 0, name = null)
        )
    }

    @Test
    fun getCharacters_apiItemsAreFilteredByName() = runTest {
        assertEquals(
            listOf(characters[1]),
            characterRemoteDataSource.getCharacters(page = 0, name = "Morty")
        )
    }

    private class FakeRickAndMortyApi(
        private val characters: List<ServerCharacter>,
    ) : RickAndMortyApi {

        override suspend fun getCharacters(page: Int, name: String?): ServerGetCharacters {
            val characters = if (name != null) {
                characters.filter { it.name.contains(name) }
            } else {
                characters
            }
            return ServerGetCharacters(characters)
        }
    }
}