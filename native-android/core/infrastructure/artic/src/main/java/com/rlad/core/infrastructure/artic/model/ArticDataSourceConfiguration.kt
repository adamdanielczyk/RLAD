package com.rlad.core.infrastructure.artic.model

import android.app.Application
import com.rlad.core.infrastructure.artic.R
import com.rlad.core.infrastructure.common.model.DataSource
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.common.model.DataSourceKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject

@ContributesIntoMap(AppScope::class)
@DataSourceKey(DataSource.ARTIC)
@Inject
class ArticDataSourceConfiguration(
    private val application: Application,
) : DataSourceConfiguration {

    override fun getDataSourcePickerText(): String = application.getString(R.string.data_source_picker_artic)
}
