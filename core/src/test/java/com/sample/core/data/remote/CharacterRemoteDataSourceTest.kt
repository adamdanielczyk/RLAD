package com.sample.core.data.remote

import com.sample.common.test.di.DaggerTestCoreComponent
import com.sample.core.data.remote.ServerCharacter.*
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
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

    private lateinit var characterRemoteDataSource: CharacterRemoteDataSource

    @Before
    fun setup() {
        val component = DaggerTestCoreComponent.builder()
            .database(mockk())
            .build()
        characterRemoteDataSource = component.characterRemoteDataSource()
        component.fakeRickAndMortyApi().addCharacters(characters)
    }

    @Test
    fun getCharacters_allApiItemsAreReturned() = runBlockingTest {
        assertEquals(
            characters,
            characterRemoteDataSource.getCharacters(page = 0, name = null)
        )
    }

    @Test
    fun getCharacters_apiItemsAreFilteredByName() = runBlockingTest {
        assertEquals(
            listOf(characters[1]),
            characterRemoteDataSource.getCharacters(page = 0, name = "Morty")
        )
    }
}