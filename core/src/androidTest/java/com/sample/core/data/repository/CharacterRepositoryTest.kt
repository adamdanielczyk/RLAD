package com.sample.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sample.common.test.di.DaggerTestCoreComponent
import com.sample.core.data.local.CharacterDatabase
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterLocalDataSource
import com.sample.core.data.remote.ServerCharacter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterRepositoryTest {

    @Rule @JvmField val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var characterRepository: CharacterRepository
    private lateinit var characterLocalDataSource: CharacterLocalDataSource

    private val databaseCharacters: List<CharacterEntity> =
        (0 until CharacterRepository.PAGE_SIZE)
            .map { createFakeCharacter(id = it, source = "database") }
            .map(::CharacterEntity)

    @Before
    fun setup() = runBlockingTest {
        val inMemoryDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CharacterDatabase::class.java
        ).build()

        val component = DaggerTestCoreComponent.builder()
            .database(inMemoryDatabase)
            .build()

        characterRepository = component.characterRepository()
        characterLocalDataSource = component.characterLocalDataSource()
    }

    @Test
    fun getCharacterById_loadCharacterFromDb() = runBlockingTest {
        addDatabaseCharacters()

        val expectedCharacter = databaseCharacters.first()
        val characterFlow = characterRepository.getCharacterBy(expectedCharacter.id)

        assertEquals(expectedCharacter, characterFlow.first())
    }

    private suspend fun addDatabaseCharacters(characters: List<CharacterEntity> = databaseCharacters) {
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

    companion object {

        private const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
        private const val INITIAL_PAGING_LOAD_SIZE =
            CharacterRepository.PAGE_SIZE * DEFAULT_INITIAL_PAGE_MULTIPLIER
    }
}