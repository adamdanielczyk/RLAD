package com.rlad.feature.details

import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.domain.usecase.GetItemByIdUseCase
import com.rlad.feature.details.ui.DetailsViewModel
import com.rlad.feature.details.ui.cachedImageMimeType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsViewModelTest {

    @Test
    fun cachedImageMimeType_readsCoil3Headers() {
        val metadata = "200\n100\n200\n2\nContent-Type:image/gif\ncache-control:max-age=3600\n"

        assertEquals("image/gif", cachedImageMimeType(metadata))
    }

    @Test
    fun cachedImageMimeType_handlesLegacyWhitespaceAndParameters() {
        assertEquals("image/webp", cachedImageMimeType("content-type: image/webp; charset=binary\n"))
    }

    @Test
    fun cachedImageMimeType_defaultsWhenContentTypeIsAbsent() {
        assertEquals("image/*", cachedImageMimeType("200\n100\n200\n0\n"))
    }

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
