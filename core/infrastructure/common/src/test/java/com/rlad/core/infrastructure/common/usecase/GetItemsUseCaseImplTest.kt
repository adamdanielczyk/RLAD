package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.infrastructure.common.repository.CommonRepository
import com.rlad.core.infrastructure.common.repository.createCommonRepository
import com.rlad.core.testing.paging.collectData
import com.rlad.core.testing.rule.TestDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GetItemsUseCaseImplTest {

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

    private val commonRepository = createCommonRepository(items)

    private val useCase = GetItemsUseCaseImpl(
        getCommonRepositoryUseCase = object : GetCommonRepositoryUseCase {
            override fun invoke(): Flow<CommonRepository> = flowOf(commonRepository)
        }
    )

    @Test
    fun allItemsAreReturnedIfQueryIsNull() = runTest {
        assertEquals(
            items,
            useCase(query = null).first().collectData()
        )
    }

    @Test
    fun searchedItemIsReturnedWhenQueryIsNotNull() = runTest {
        assertEquals(
            listOf(items[1]),
            useCase(query = "name2").first().collectData()
        )
    }
}