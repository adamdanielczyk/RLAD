package com.sample.infrastructure.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sample.infrastructure.remote.ServerCharacter
import com.sample.infrastructure.repository.CharacterRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CharacterLocalDataSourceTest {

    @Rule @JvmField val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val characterDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CharacterDatabase::class.java
    ).build()

    private val characterDao = characterDatabase.characterDao()

    private val characterLocalDataSource = CharacterLocalDataSource(characterDao)

    private val databaseCharacters: List<CharacterEntity> =
        (0 until CharacterRepository.PAGE_SIZE)
            .map { createFakeCharacter(id = it, source = "database") }
            .map(::CharacterEntity)

    @Test
    fun getCharacterById_loadCharacterFromDb() = runTest {
        addDatabaseCharacters(databaseCharacters)

        val expectedCharacter = databaseCharacters.first()
        val characterFlow = characterLocalDataSource.getCharacterBy(expectedCharacter.id)

        assertEquals(expectedCharacter, characterFlow.first())
    }

    private suspend fun addDatabaseCharacters(characters: List<CharacterEntity>) {
        characterLocalDataSource.insertCharacters(characters)
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