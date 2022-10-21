package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.infrastructure.common.model.DataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllDataSourcesUseCaseImplTest {

    @Test
    fun repositoriesAreSortedByDataSourceName() {
        val useCase = GetAllDataSourcesUseCaseImpl()

        assertEquals(
            DataSource.values().sortedBy { it.dataSourceName },
            useCase(),
        )
    }
}