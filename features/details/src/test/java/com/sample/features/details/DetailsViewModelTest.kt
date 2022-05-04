package com.sample.features.details

import androidx.lifecycle.SavedStateHandle
import com.sample.domain.model.ItemUiModel
import com.sample.domain.usecase.GetSelectedItemsRepositoryUseCase
import com.sample.features.details.ui.DetailsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest {

    private val getSelectedItemsRepositoryUseCase = mockk<GetSelectedItemsRepositoryUseCase>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getItem_returnMatchingRepositoryItem() = runTest {
        val item = ItemUiModel(
            id = "1",
            imageUrl = "url 1",
            name = "Name",
            cardCaptions = listOf("test1"),
            detailsKeyValues = listOf(
                { "test2" } to { "test3" }
            ),
        )
        coEvery { getSelectedItemsRepositoryUseCase().getItemBy(id = "1") } returns flowOf(item)
        every { savedStateHandle.get<String>("id") } returns "1"

        val viewModel = DetailsViewModel(getSelectedItemsRepositoryUseCase, savedStateHandle)

        advanceUntilIdle()

        assertEquals(item, viewModel.item.first())
    }
}