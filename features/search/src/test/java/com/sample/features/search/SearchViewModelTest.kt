package com.sample.features.search

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterEntity.Gender
import com.sample.core.data.local.CharacterEntity.Location
import com.sample.core.data.local.CharacterEntity.Status
import com.sample.core.data.repository.CharacterRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private val characters = listOf(
        CharacterEntity(
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
        CharacterEntity(
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

    private val searchedCharacters = listOf(characters[0])

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        val characterRepository = mockk<CharacterRepository>()

        every {
            characterRepository.getCharacters(nameOrLocation = null)
        } returns flowOf(PagingData.from(characters))

        every {
            characterRepository.getCharacters(nameOrLocation = "Rick")
        } returns flowOf(PagingData.from(searchedCharacters))

        viewModel = SearchViewModel(characterRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun allCharactersDisplayedByDefault() = runTest {
        assertEquals(characters, viewModel.getCurrentCharacters())
    }

    @Test
    fun onItemClicked_sendOpenDetailsEvent() = runTest {
        val character = characters.first()

        viewModel.onItemClicked(character)

        val clickedCharacter = viewModel.openDetailsScreen.first()
        assertEquals(character, clickedCharacter)
    }

    @Test
    fun onQueryTextChanged_emitItemsAfterDebounce() = runTest {
        viewModel.onQueryTextChanged("Rick")

        assertEquals(characters, viewModel.getCurrentCharacters())

        advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)
        runCurrent()

        assertEquals(characters, viewModel.getCurrentCharacters())

        advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)
        runCurrent()

        assertEquals(searchedCharacters, viewModel.getCurrentCharacters())
    }

    @Test
    fun onSearchExpanded_scrollToTop() = runTest {
        viewModel.onSearchExpanded()

        assertEquals(Unit, viewModel.scrollToTop.first())
    }

    @Test
    fun onSearchCollapsed_scrollToTop() = runTest {
        viewModel.onSearchCollapsed()

        assertEquals(Unit, viewModel.scrollToTop.first())
    }

    @Test
    fun onClearSearchClicked_onSearchCollapsed_displayAllCharactersAndScrollToTop() = runTest {
        viewModel.onClearSearchClicked()

        assertEquals(Unit, viewModel.scrollToTop.first())
        assertEquals(characters, viewModel.getCurrentCharacters())
    }

    private suspend fun SearchViewModel.getCurrentCharacters(): List<CharacterEntity> =
        charactersPagingData.first().first().collectData()
}

private val differCallback = object : DifferCallback {
    override fun onChanged(position: Int, count: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

private suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
    val items = mutableListOf<T>()
    val differ = object : PagingDataDiffer<T>(differCallback) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit,
        ): Int? {
            for (idx in 0 until newList.size) {
                items.add(newList.getFromStorage(idx))
            }
            onListPresentable()
            return null
        }
    }
    differ.collectFrom(this)
    return items
}