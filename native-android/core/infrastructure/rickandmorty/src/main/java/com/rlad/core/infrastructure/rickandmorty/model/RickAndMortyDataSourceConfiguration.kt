package com.rlad.core.infrastructure.rickandmorty.model

import android.app.Application
import com.rlad.core.domain.model.DataSource
import com.rlad.core.domain.model.DataSourceKey
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.rickandmorty.R
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap

@ContributesIntoMap(AppScope::class)
@DataSourceKey(DataSource.RICKANDMORTY)
class RickAndMortyDataSourceConfiguration(
    private val application: Application,
) : DataSourceConfiguration {

    override fun getDataSourcePickerText(): String = application.getString(R.string.data_source_picker_rickandmorty)
}
