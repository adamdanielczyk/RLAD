package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.repository.AppSettingsRepository
import com.rlad.core.infrastructure.common.repository.ItemsRepository
import com.rlad.core.infrastructure.common.repository.createItemsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetSelectedItemsRepositoryUseCaseImplTest {

    private val repositories: List<ItemsRepository> = listOf(
        createItemsRepository(dataSourceName = "name1", pickerText = "picker1"),
        createItemsRepository(dataSourceName = "name2", pickerText = "picker2"),
    )

    private val appSettingsRepository: AppSettingsRepository = mockk()

    private val useCase = GetSelectedItemsRepositoryUseCaseImpl(
        getAllItemsRepositoriesUseCase = object : GetAllItemsRepositoriesUseCase {
            override fun invoke(): List<ItemsRepository> = repositories
        },
        appSettingsRepository = appSettingsRepository,
    )

    @Test
    fun selectedRepositoryIsReturned() = runTest {
        every {
            appSettingsRepository.getSelectedDataSourceName()
        } returns flowOf("name2")

        assertEquals(
            repositories[1].getDataSourceName(),
            useCase().first().getDataSourceName()
        )
    }

    @Test
    fun firstRepositoryIsReturnedIfNoSelectionWasMade() = runTest {
        every {
            appSettingsRepository.getSelectedDataSourceName()
        } returns flowOf(null)

        assertEquals(
            repositories[0].getDataSourceName(),
            useCase().first().getDataSourceName()
        )
    }
}