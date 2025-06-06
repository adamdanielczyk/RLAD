package com.rlad.core.infrastructure.rickandmorty.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity.Gender
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity.Location
import com.rlad.core.infrastructure.rickandmorty.local.CharacterEntity.Status
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RickAndMortyLocalDataSourceTest {

    private lateinit var localDataSource: RickAndMortyLocalDataSource

    private val characters: List<CharacterEntity> = (1..5).map(::createFakeCharacter)

    @Before
    fun createDb() = runTest {
        val database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RickAndMortyDatabase::class.java,
        ).build()

        localDataSource = RickAndMortyLocalDataSource(database.characterDao())
        localDataSource.insert(characters)
    }

    @Test
    fun getCharacterById_loadCharacterFromDb() = runTest {
        assertEquals(
            characters.first(),
            localDataSource.getById(id = "1").first(),
        )
    }

    @Test
    fun getCharacterById_returnNullIfDoesNotExist() = runTest {
        assertEquals(
            null,
            localDataSource.getById(id = "10").first(),
        )
    }

    private fun createFakeCharacter(id: Int) = CharacterEntity(
        id = id,
        name = "Rick Sanchez",
        status = Status.ALIVE,
        species = "species",
        type = "type",
        gender = Gender.MALE,
        location = Location("location"),
        imageUrl = "url",
        created = "created",
    )
}
