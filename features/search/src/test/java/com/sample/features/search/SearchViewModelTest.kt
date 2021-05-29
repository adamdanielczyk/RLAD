package com.sample.features.search

import android.widget.ImageView
import androidx.paging.PagingData
import com.sample.common.test.utils.collectData
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.repository.CharacterRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope()

    private val characters = listOf(
        CharacterEntity(
            id = 1,
            name = "Rick Sanchez",
            status = CharacterEntity.Status.ALIVE,
            species = "species 1",
            type = "type 1",
            gender = CharacterEntity.Gender.MALE,
            location = CharacterEntity.Location("location 1"),
            imageUrl = "url 1",
            created = "created 1"
        ),
        CharacterEntity(
            id = 2,
            name = "Morty Smith",
            status = CharacterEntity.Status.ALIVE,
            species = "species 2",
            type = "type 2",
            gender = CharacterEntity.Gender.MALE,
            location = CharacterEntity.Location("location 2"),
            imageUrl = "url 2",
            created = "created 2"
        )
    )

    private val searchedCharacters = listOf(characters[0])

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        val characterRepository = mockk<CharacterRepository>()

        every {
            characterRepository.getCharacters(nameOrLocation = null)
        } returns flowOf(PagingData.from(characters))

        every {
            characterRepository.getCharacters(nameOrLocation = "Rick")
        } returns flowOf(PagingData.from(searchedCharacters))

        viewModel = SearchViewModel(characterRepository)
    }

    @Test
    fun allCharactersDisplayedByDefault() = runBlockingTest {
        assertEquals(characters, viewModel.getCurrentCharacters())
    }

    @Test
    fun onItemClicked_sendOpenDetailsEvent() = runBlockingTest {
        val imageView = mockk<ImageView>()
        val character = characters.first()

        viewModel.openDetailsScreen
            .onEach { (clickedCharacter, clickedImageView) ->
                assertEquals(character, clickedCharacter)
                assertEquals(imageView, clickedImageView)
            }
            .launchIn(testCoroutineScope)

        viewModel.onItemClicked(character, imageView)
    }

    @Test
    fun onQueryTextChanged_emitItemsAfterDebounce() = runBlockingTest {
        viewModel.onQueryTextChanged("Rick")

        assertEquals(characters, viewModel.getCurrentCharacters())

        testCoroutineDispatcher.advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)

        assertEquals(characters, viewModel.getCurrentCharacters())

        testCoroutineDispatcher.advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)

        assertEquals(searchedCharacters, viewModel.getCurrentCharacters())
    }

    @Test
    fun onSearchExpanded_scrollToTop() {
        viewModel.scrollToTop
            .onEach { event -> assertEquals(Unit, event) }
            .launchIn(testCoroutineScope)

        viewModel.onSearchExpanded()
    }

    @Test
    fun onSearchCollapsed_displayAllCharactersAndScrollToTop() = runBlockingTest {
        viewModel.scrollToTop
            .onEach { event -> assertEquals(Unit, event) }
            .launchIn(testCoroutineScope)

        viewModel.onSearchCollapsed()

        assertEquals(characters, viewModel.getCurrentCharacters())
    }

    @Test
    fun onClearSearchClicked_scrollToTop() {
        viewModel.scrollToTop
            .onEach { event -> assertEquals(Unit, event) }
            .launchIn(testCoroutineScope)

        viewModel.onClearSearchClicked()
    }

    private suspend fun SearchViewModel.getCurrentCharacters(): List<CharacterEntity> =
        charactersUpdates.first().first().collectData()
}