package com.rlad.infrastructure.common.usecase

import com.rlad.domain.model.DataSourceUiModel
import com.rlad.infrastructure.common.repository.ItemsRepository
import com.rlad.infrastructure.common.repository.createItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAvailableDataSourcesUseCaseImplTest {

    private val repositories: List<ItemsRepository> = listOf(
        createItemsRepository(dataSourceName = "name1", pickerText = "picker1"),
        createItemsRepository(dataSourceName = "name2", pickerText = "picker2"),
    )

    @Test
    fun repositoriesAreMappedToUiModelsAndSelectedRepositoryIsMarked() = runTest {
        val useCase = GetAvailableDataSourcesUseCaseImpl(
            getAllItemsRepositoriesUseCase = object : GetAllItemsRepositoriesUseCase {
                override fun invoke(): List<ItemsRepository> = repositories
            },
            getSelectedItemsRepositoryUseCase = object : GetSelectedItemsRepositoryUseCase {
                override fun invoke(): Flow<ItemsRepository> = flowOf(repositories[1])
            }
        )

        assertEquals(
            listOf(
                DataSourceUiModel(name = "name1", pickerText = "picker1", isSelected = false),
                DataSourceUiModel(name = "name2", pickerText = "picker2", isSelected = true),
            ),
            useCase().first()
        )
    }
}