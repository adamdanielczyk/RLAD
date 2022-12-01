package com.rlad.core.infrastructure.rickandmorty.model

import android.app.Application
import com.rlad.core.infrastructure.common.model.DataSourceConfiguration
import com.rlad.core.infrastructure.rickandmorty.R
import javax.inject.Inject

internal class RickAndMortyDataSourceConfiguration @Inject constructor(
    private val application: Application,
) : DataSourceConfiguration {

    override fun getDataSourcePickerText(): String = application.getString(R.string.data_source_picker_rickandmorty)
}