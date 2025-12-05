package com.rlad.feature.details

import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import com.rlad.feature.details.ui.DetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsViewModelTest {

    private val getItemByIdUseCase = mockk<GetItemByIdUseCase>()

    @Test
    fun getItem_returnMatchingRepositoryItem() = runTest {
        val item = ItemUiModel(
            id = "1",
            imageUrl = "url 1",
            name = "Name",
            cardCaption = "test1",
            detailsKeyValues = listOf("test2" to "test3"),
        )
        coEvery { getItemByIdUseCase(id = "1") } returns flowOf(item)

        val viewModel = DetailsViewModel(getItemByIdUseCase = getItemByIdUseCase, id = "1")

        assertEquals(item, viewModel.item.first())
    }
}
