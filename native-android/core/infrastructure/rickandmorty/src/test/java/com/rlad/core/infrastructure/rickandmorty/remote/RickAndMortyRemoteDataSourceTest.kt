package com.rlad.core.infrastructure.rickandmorty.remote

import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter.Gender
import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter.Location
import com.rlad.core.infrastructure.rickandmorty.remote.ServerCharacter.Status
import io.mockk.coEvery
import io.mockk.mockk
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
            created = "created 1",
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
            created = "created 2",
        ),
    )

    private val mockRickAndMortyApi = mockk<RickAndMortyApi>()
    private val remoteDataSource = RickAndMortyRemoteDataSource(mockRickAndMortyApi)

    @Test
    fun getCharacters_allApiItemsAreReturned() = runTest {
        coEvery { mockRickAndMortyApi.getCharacters(page = 0, name = null) } returns ServerGetCharacters(characters)
        
        assertEquals(
            ServerGetCharacters(characters),
            remoteDataSource.getRootData(offset = 0, pageSize = 10),
        )
    }

    @Test
    fun getCharacters_apiItemsAreFilteredByName() = runTest {
        coEvery { mockRickAndMortyApi.getCharacters(page = 0, name = "Morty") } returns ServerGetCharacters(listOf(characters[1]))
        
        assertEquals(
            ServerGetCharacters(listOf(characters[1])),
            remoteDataSource.search(query = "Morty", offset = 0, pageSize = 10),
        )
    }

    @Test
    fun getCharacter_itemWithMatchingIdIsReturned() = runTest {
        coEvery { mockRickAndMortyApi.getCharacter(id = 2) } returns characters[1]
        
        assertEquals(
            characters[1],
            remoteDataSource.getItem(id = "2"),
        )
    }

}
