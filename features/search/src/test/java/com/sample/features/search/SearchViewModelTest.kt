package com.sample.features.search

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.ItemsRepository
import com.sample.features.search.ui.SearchViewModel
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

    private val items = listOf(
        ItemUiModel(
            id = 1,
            imageUrl = "url 1",
            name = "Rick Sanchez",
            cardCaptions = listOf("test1"),
            detailsKeyValues = listOf(
                { "test2" } to { "test3" }
            ),
        ),
        ItemUiModel(
            id = 2,
            imageUrl = "url 2",
            name = "Morty Smith",
            cardCaptions = listOf("test1"),
            detailsKeyValues = listOf(
                { "test2" } to { "test3" }
            ),
        )
    )

    private val searchedItems = listOf(items[0])

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        val itemsRepository = mockk<ItemsRepository>()

        every {
            itemsRepository.getItems(name = null)
        } returns flowOf(PagingData.from(items))

        every {
            itemsRepository.getItems(name = "Rick")
        } returns flowOf(PagingData.from(searchedItems))

        viewModel = SearchViewModel(itemsRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun allItemsDisplayedByDefault() = runTest {
        assertEquals(items, viewModel.getCurrentItems())
    }

    @Test
    fun onQueryTextChanged_emitItemsAfterDebounce() = runTest {
        viewModel.onQueryTextChanged("Rick")

        assertEquals(items, viewModel.getCurrentItems())

        advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)
        runCurrent()

        assertEquals(items, viewModel.getCurrentItems())

        advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)
        runCurrent()

        assertEquals(searchedItems, viewModel.getCurrentItems())
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
    fun onClearSearchClicked_onSearchCollapsed_displayAllItemsAndScrollToTop() = runTest {
        viewModel.onClearSearchClicked()

        assertEquals(Unit, viewModel.scrollToTop.first())
        assertEquals(items, viewModel.getCurrentItems())
    }

    private suspend fun SearchViewModel.getCurrentItems(): List<ItemUiModel> =
        itemsPagingData.first().first().collectData()
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