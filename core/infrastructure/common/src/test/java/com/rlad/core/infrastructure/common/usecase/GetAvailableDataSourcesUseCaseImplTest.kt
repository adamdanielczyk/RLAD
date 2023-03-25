package com.rlad.core.infrastructure.common.usecase

import com.rlad.core.domain.model.DataSourceUiModel
import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAvailableDataSourcesUseCaseImplTest {

    @Test
    fun repositoriesAreMappedToUiModelsAndSelectedRepositoryIsMarked() = runTest {
        val useCase = GetAvailableDataSourcesUseCaseImpl(
            getAllDataSourcesUseCase = GetAllDataSourcesUseCaseImpl(),
            getSelectedDataSourceUseCase = object : GetSelectedDataSourceUseCase {
                override fun invoke(): Flow<DataSource> = flowOf(DataSource.RICKANDMORTY)
            },
            dataSourceConfigurations = mapOf(
                DataSource.ARTIC to object : DataSourceConfiguration {
                    override fun getDataSourcePickerText(): String = "picker1"
                },
                DataSource.GIPHY to object : DataSourceConfiguration {
                    override fun getDataSourcePickerText(): String = "picker2"
                },
                DataSource.RICKANDMORTY to object : DataSourceConfiguration {
                    override fun getDataSourcePickerText(): String = "picker3"
                },
            )
        )

        assertEquals(
            listOf(
                DataSourceUiModel(name = DataSource.ARTIC.dataSourceName, pickerText = "picker1", isSelected = false),
                DataSourceUiModel(name = DataSource.GIPHY.dataSourceName, pickerText = "picker2", isSelected = false),
                DataSourceUiModel(name = DataSource.RICKANDMORTY.dataSourceName, pickerText = "picker3", isSelected = true),
            ),
            useCase().first()
        )
    }
}