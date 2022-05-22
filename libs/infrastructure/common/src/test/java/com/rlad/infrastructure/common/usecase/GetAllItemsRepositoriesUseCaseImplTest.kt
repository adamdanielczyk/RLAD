package com.rlad.infrastructure.common.usecase

import com.rlad.infrastructure.common.repository.ItemsRepository
import com.rlad.infrastructure.common.repository.createItemsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllItemsRepositoriesUseCaseImplTest {

    private val repositories: Set<ItemsRepository> = setOf(
        createItemsRepository(dataSourceName = "name2", pickerText = "picker2"),
        createItemsRepository(dataSourceName = "name1", pickerText = "picker1"),
    )

    @Test
    fun repositoriesAreSortedByDataSourceName() {
        val useCase = GetAllItemsRepositoriesUseCaseImpl(repositories)

        assertEquals(
            repositories.map { it.getDataSourceName() }.sorted(),
            useCase().map { it.getDataSourceName() },
        )
    }
}