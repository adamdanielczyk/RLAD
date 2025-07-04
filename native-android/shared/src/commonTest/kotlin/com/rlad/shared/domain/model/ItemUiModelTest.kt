package com.rlad.shared.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ItemUiModelTest {

    @Test
    fun createItemUiModel_shouldHaveCorrectProperties() {
        val item = ItemUiModel(
            id = "1",
            imageUrl = "https://example.com/image.jpg",
            name = "Test Item",
            cardCaption = "Test Caption",
            detailsKeyValues = listOf("key1" to "value1", "key2" to "value2")
        )

        assertEquals("1", item.id)
        assertEquals("https://example.com/image.jpg", item.imageUrl)
        assertEquals("Test Item", item.name)
        assertEquals("Test Caption", item.cardCaption)
        assertEquals(2, item.detailsKeyValues.size)
    }
}