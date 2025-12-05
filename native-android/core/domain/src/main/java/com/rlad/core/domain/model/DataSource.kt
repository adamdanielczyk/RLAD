package com.rlad.core.domain.model

import dev.zacsweers.metro.MapKey

enum class DataSource(val dataSourceName: String) {
    ARTIC("ARTIC"),
    GIPHY("GIPHY"),
    RICKANDMORTY("RICK_AND_MORTY");

    companion object {
        fun fromString(dataSourceName: String) = entries.first { it.dataSourceName == dataSourceName }
    }
}

@MapKey
annotation class DataSourceKey(val value: DataSource)
