package com.sample.infrastructure.rickandmorty.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sample.infrastructure.rickandmorty.remote.ServerCharacter
import com.sample.infrastructure.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RickAndMortyLocalDataSourceTest {

    @Rule @JvmField val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val rickAndMortyDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        RickAndMortyDatabase::class.java
    ).build()

    private val characterDao = rickAndMortyDatabase.characterDao()

    private val localDataSource = RickAndMortyLocalDataSource(characterDao)

    private val databaseCharacters: List<CharacterEntity> =
        (0 until RickAndMortyRepository.PAGE_SIZE)
            .map { createFakeCharacter(id = it, source = "database") }
            .map(::CharacterEntity)

    @Test
    fun getCharacterById_loadCharacterFromDb() = runTest {
        addDatabaseCharacters(databaseCharacters)

        val expectedCharacter = databaseCharacters.first()
        val characterFlow = localDataSource.getCharacterBy(expectedCharacter.id)

        assertEquals(expectedCharacter, characterFlow.first())
    }

    private suspend fun addDatabaseCharacters(characters: List<CharacterEntity>) {
        localDataSource.insertCharacters(characters)
    }

    private fun createFakeCharacter(id: Int, source: String): ServerCharacter {
        val sourceWithId = "$source-$id"
        return ServerCharacter(
            id = id,
            name = "Rick Sanchez",
            status = ServerCharacter.Status.ALIVE,
            species = "species $sourceWithId",
            type = "type $sourceWithId",
            gender = ServerCharacter.Gender.MALE,
            location = ServerCharacter.Location("location $sourceWithId"),
            imageUrl = "url $sourceWithId",
            created = "created $sourceWithId"
        )
    }
}