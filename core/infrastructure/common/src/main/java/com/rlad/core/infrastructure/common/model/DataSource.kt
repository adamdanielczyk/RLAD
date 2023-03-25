package com.rlad.core.infrastructure.common.model

import dagger.MapKey

enum class DataSource(val dataSourceName: String) {
    ARTIC("ARTIC"),
    GIPHY("GIPHY"),
    RICKANDMORTY("RICK_AND_MORTY");

    companion object {
        fun fromString(dataSourceName: String) = values().first { it.dataSourceName == dataSourceName }
    }
}

@MapKey
annotation class DataSourceKey(val value: DataSource)