package com.rlad.features.search

import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.rlad.domain.model.ItemUiModel
import com.rlad.domain.usecase.GetItemsUseCase
import com.rlad.features.search.ui.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
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
            id = "1",
            imageUrl = "url 1",
            name = "name1",
            cardCaptions = listOf("test1"),
            detailsKeyValues = listOf("test2" to "test3"),
        ),
        ItemUiModel(
            id = "2",
            imageUrl = "url 2",
            name = "name2",
            cardCaptions = listOf("test1"),
            detailsKeyValues = listOf("test2" to "test3"),
        )
    )

    private val searchedItem = listOf(items.first())

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        val getItemsUseCase = mockk<GetItemsUseCase>()

        coEvery {
            getItemsUseCase(query = null)
        } returns flowOf(PagingData.from(items))

        coEvery {
            getItemsUseCase(query = "name1")
        } returns flowOf(PagingData.from(searchedItem))

        viewModel = SearchViewModel(
            getAvailableDataSourcesUseCase = mockk(),
            getItemsUseCase = getItemsUseCase,
            appSettingsRepository = mockk(),
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun allItemsDisplayedByDefault() = runTest {
        advanceUntilIdle()
        assertEquals(items, viewModel.getCurrentItems())
    }

    @Test
    fun onQueryTextChanged_emitItemsAfterDebounce() = runTest {
        viewModel.onQueryTextChanged("name1")

        runCurrent()

        assertEquals(items, viewModel.getCurrentItems())

        advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)
        runCurrent()

        assertEquals(items, viewModel.getCurrentItems())

        advanceTimeBy(SearchViewModel.DEBOUNCE_TIME / 2)
        runCurrent()

        assertEquals(searchedItem, viewModel.getCurrentItems())
    }

    @Test
    fun onSearchFocused_scrollToTop() = runTest {
        viewModel.onSearchFocused()

        assertEquals(Unit, viewModel.scrollToTop.first())
    }

    @Test
    fun onClearSearchClicked_onSearchCollapsed_displayAllItemsAndScrollToTop() = runTest {
        viewModel.onClearSearchClicked()

        assertEquals(Unit, viewModel.scrollToTop.first())
        assertEquals(items, viewModel.getCurrentItems())
    }

    private suspend fun SearchViewModel.getCurrentItems(): List<ItemUiModel> =
        itemsPagingData.value?.first()?.collectData().orEmpty()
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