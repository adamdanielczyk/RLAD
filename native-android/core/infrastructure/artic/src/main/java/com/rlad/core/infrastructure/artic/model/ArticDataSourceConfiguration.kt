package com.rlad.core.infrastructure.artic.model

import android.app.Application
import com.rlad.core.infrastructure.artic.R
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import javax.inject.Inject

internal class ArticDataSourceConfiguration @Inject constructor(
    private val application: Application,
) : DataSourceConfiguration {

    override fun getDataSourcePickerText(): String = application.getString(R.string.data_source_picker_artic)
}