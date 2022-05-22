package com.rlad.features.search

import androidx.paging.PagingData
import com.rlad.domain.model.DataSourceUiModel
import com.rlad.domain.model.ItemUiModel
import com.rlad.domain.repository.AppSettingsRepository
import com.rlad.domain.usecase.GetItemsUseCase
import com.rlad.features.search.ui.SearchViewModel
import com.rlad.testutils.paging.collectData
import com.rlad.testutils.rule.TestDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @Rule @JvmField val dispatcherRule = TestDispatcherRule()

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

    private val appSettingsRepository: AppSettingsRepository = mockk()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
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
            appSettingsRepository = appSettingsRepository,
        )
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
    fun onScrollToTopClicked_scrollToTop() = runTest {
        viewModel.onScrollToTopClicked()

        assertEquals(Unit, viewModel.scrollToTop.first())
    }

    @Test
    fun onClearSearchClicked_onSearchCollapsed_displayAllItemsAndScrollToTop() = runTest {
        viewModel.onClearSearchClicked()

        assertEquals(Unit, viewModel.scrollToTop.first())
        assertEquals(items, viewModel.getCurrentItems())
    }

    @Test
    fun onDataSourceClicked_saveSelectedDataSourceAndDisplayAllItems() = runTest {
        viewModel.onDataSourceClicked(DataSourceUiModel(
            name = "name", pickerText = "picker text", isSelected = true
        ))

        advanceUntilIdle()

        coVerify {
            appSettingsRepository.saveSelectedDataSourceName(dataSourceName = "name")
        }
        assertEquals(items, viewModel.getCurrentItems())
    }

    private suspend fun SearchViewModel.getCurrentItems(): List<ItemUiModel> =
        itemsPagingData.value?.first()?.collectData().orEmpty()
}