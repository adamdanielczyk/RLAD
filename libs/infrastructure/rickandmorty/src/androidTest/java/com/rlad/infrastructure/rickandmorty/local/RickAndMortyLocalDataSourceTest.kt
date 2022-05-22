package com.rlad.infrastructure.rickandmorty.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rlad.infrastructure.rickandmorty.remote.ServerCharacter
import com.rlad.infrastructure.rickandmorty.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RickAndMortyLocalDataSourceTest {

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
        val characterFlow = localDataSource.getCharacterById(expectedCharacter.id)

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