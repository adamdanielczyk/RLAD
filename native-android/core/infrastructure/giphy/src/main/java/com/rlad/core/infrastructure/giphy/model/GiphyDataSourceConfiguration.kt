package com.rlad.core.infrastructure.giphy.model

import android.app.Application
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.giphy.R
import javax.inject.Inject

internal class GiphyDataSourceConfiguration @Inject constructor(
    private val application: Application,
) : DataSourceConfiguration {

    override fun getDataSourcePickerText(): String = application.getString(R.string.data_source_picker_giphy)
}