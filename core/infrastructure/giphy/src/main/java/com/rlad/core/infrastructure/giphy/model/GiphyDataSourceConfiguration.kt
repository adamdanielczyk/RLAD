package com.rlad.core.infrastructure.giphy.model

import android.content.Context
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.giphy.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class GiphyDataSourceConfiguration @Inject constructor(
    @ApplicationContext private val context: Context,
) : DataSourceConfiguration {

    override fun getDataSourcePickerText(): String = context.getString(R.string.data_source_picker_giphy)
}