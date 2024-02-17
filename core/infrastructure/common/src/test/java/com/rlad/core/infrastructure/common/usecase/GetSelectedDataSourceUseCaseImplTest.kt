package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.repository.AppSettingsRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetSelectedDataSourceUseCaseImplTest {

    private val appSettingsRepository: AppSettingsRepositoryImpl = mockk()

    private val useCase = GetSelectedDataSourceUseCaseImpl(
        getAllDataSourcesUseCase = GetAllDataSourcesUseCaseImpl(),
        appSettingsRepository = appSettingsRepository,
    )

    @Test
    fun selectedRepositoryIsReturned() = runTest {
        every {
            appSettingsRepository.getSelectedDataSource()
        } returns flowOf(DataSource.values()[1])

        assertEquals(
            DataSource.values()[1],
            useCase().first(),
        )
    }

    @Test
    fun giphyRepositoryIsReturnedIfNoSelectionWasMade() = runTest {
        every {
            appSettingsRepository.getSelectedDataSource()
        } returns flowOf(null)

        assertEquals(
            DataSource.GIPHY,
            useCase().first(),
        )
    }
}
