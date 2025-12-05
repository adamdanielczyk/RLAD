package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.DataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllDataSourcesUseCaseTest {

    @Test
    fun repositoriesAreSortedByDataSourceName() {
        val useCase = GetAllDataSourcesUseCase()

        assertEquals(
            DataSource.entries.sortedBy { it.dataSourceName },
            useCase(),
        )
    }
}
