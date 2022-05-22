package com.rlad.infrastructure.rickandmorty.remote

import com.rlad.infrastructure.rickandmorty.remote.ServerCharacter.Gender
import com.rlad.infrastructure.rickandmorty.remote.ServerCharacter.Location
import com.rlad.infrastructure.rickandmorty.remote.ServerCharacter.Status
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RickAndMortyRemoteDataSourceTest {

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
    private val remoteDataSource = RickAndMortyRemoteDataSource(fakeRickAndMortyApi)

    @Test
    fun getCharacters_allApiItemsAreReturned() = runTest {
        assertEquals(
            characters,
            remoteDataSource.getCharacters(page = 0, name = null)
        )
    }

    @Test
    fun getCharacters_apiItemsAreFilteredByName() = runTest {
        assertEquals(
            listOf(characters[1]),
            remoteDataSource.getCharacters(page = 0, name = "Morty")
        )
    }

    @Test
    fun getCharacter_itemWithMatchingIdIsReturned() = runTest {
        assertEquals(
            characters[1],
            remoteDataSource.getCharacter(id = 2)
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

        override suspend fun getCharacter(id: Int): ServerCharacter {
            return characters.first { it.id == id }
        }
    }
}