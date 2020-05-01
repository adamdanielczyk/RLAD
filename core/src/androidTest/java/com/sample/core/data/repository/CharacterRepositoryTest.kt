package com.sample.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sample.core.data.di.DaggerTestCoreComponent
import com.sample.core.data.local.CharacterDatabase
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterLocalDataSource
import com.sample.core.data.remote.FakeRickAndMortyApi
import com.sample.core.data.remote.ServerCharacter
import com.sample.core.data.remote.ServerCharacter.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalStdlibApi
class CharacterRepositoryTest {

    @Rule @JvmField val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var rickAndMortyApi: FakeRickAndMortyApi
    private lateinit var characterRepository: CharacterRepository
    private lateinit var characterLocalDataSource: CharacterLocalDataSource

    private val databaseCharacters: List<CharacterEntity> =
        (0 until CharacterRepository.PAGE_SIZE)
            .map { createFakeCharacter(id = it, source = "database") }
            .map(::CharacterEntity)

    private val serverCharacters: List<ServerCharacter> =
        (databaseCharacters.size until INITIAL_PAGING_LOAD_SIZE)
            .map { createFakeCharacter(id = it, source = "server") }

    @Before
    fun setup() = runBlockingTest {
        val inMemoryDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CharacterDatabase::class.java
        ).build()

        val component = DaggerTestCoreComponent.builder()
            .database(inMemoryDatabase)
            .build()

        rickAndMortyApi = component.fakeRickAndMortyApi()
        characterRepository = component.characterRepository()
        characterLocalDataSource = component.characterLocalDataSource()
    }

    @Test
    fun getCharacters_returnEmptyListWhenApiAndDatabaseHaveNoItems() {
        val charactersLiveData = characterRepository.getCharacters(
            scope = TestCoroutineScope()
        )

        val pagedList = getPagedList(charactersLiveData)

        assertEquals(emptyList<List<ServerCharacter>>(), pagedList)
    }

    @Test
    fun getCharacters_returnInitialLoadItems() = runBlockingTest {
        addDatabaseCharacters()
        addServerCharacters()

        val charactersLiveData = characterRepository.getCharacters(
            scope = TestCoroutineScope()
        )

        val pagedList = getPagedList(charactersLiveData)
        pagedList.loadAround(0)

        val allItems = databaseCharacters + serverCharacters.map(::CharacterEntity)
        val expectedList = allItems.take(INITIAL_PAGING_LOAD_SIZE)
        assertEquals(expectedList, pagedList)
    }

    @Test
    fun getCharacters_returnAllItems() = runBlockingTest {
        addDatabaseCharacters()
        addServerCharacters()

        val charactersLiveData = characterRepository.getCharacters(
            scope = TestCoroutineScope()
        )

        val pagedList = getPagedList(charactersLiveData)
        pagedList.loadAllData()

        val expectedList = databaseCharacters + serverCharacters.map(::CharacterEntity)
        assertEquals(expectedList, pagedList)
    }

    @Test
    fun getCharacterById_loadCharacterFromDb() = runBlockingTest {
        addDatabaseCharacters()

        val expectedCharacter = databaseCharacters.first()
        val characterFlow = characterRepository.getCharacterById(expectedCharacter.id)

        assertEquals(expectedCharacter, characterFlow.first())
    }

    private suspend fun addDatabaseCharacters() {
        characterLocalDataSource.insertCharacters(databaseCharacters)
    }

    private fun addServerCharacters() {
        rickAndMortyApi.addCharacters(serverCharacters)
    }

    private fun createFakeCharacter(id: Int, source: String): ServerCharacter {
        val sourceWithId = "$source-$id"
        return ServerCharacter(
            id = id,
            name = "Rick Sanchez",
            status = Status.ALIVE,
            species = "species $sourceWithId",
            type = "type $sourceWithId",
            gender = Gender.MALE,
            location = Location("location $sourceWithId"),
            imageUrl = "url $sourceWithId",
            created = "created $sourceWithId"
        )
    }

    private fun getPagedList(liveData: LiveData<PagedList<CharacterEntity>>): PagedList<CharacterEntity> {
        val observer = LoggingObserver<PagedList<CharacterEntity>>()
        liveData.observeForever(observer)
        return observer.value!!
    }

    private class LoggingObserver<T> : Observer<T> {
        var value: T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }

    private fun <T> PagedList<T>.loadAllData() {
        do {
            val oldSize = loadedCount
            loadAround(size - 1)
        } while (size != oldSize)
    }

    companion object {

        private const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
        private const val INITIAL_PAGING_LOAD_SIZE =
            CharacterRepository.PAGE_SIZE * DEFAULT_INITIAL_PAGE_MULTIPLIER
    }
}