package com.sample.features.details

import androidx.lifecycle.SavedStateHandle
import com.sample.domain.model.ItemUiModel
import com.sample.domain.repository.ItemsRepository
import com.sample.features.details.ui.DetailsViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsViewModelTest {

    private val itemsRepository = mockk<ItemsRepository>()
    private val savedStateHandle = mockk<SavedStateHandle>()

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
        every { itemsRepository.getItemBy(id = "1") } returns flowOf(item)
        every { savedStateHandle.get<String>("id") } returns "1"

        val viewModel = DetailsViewModel(itemsRepository, savedStateHandle)

        assertEquals(item, viewModel.getItem().first())
    }
}