package com.sample.domain.model

import dagger.MapKey

enum class DataSource(val sourceName: String) {
    GIPHY("giphy"),
    RICK_AND_MORTY("rickandmorty")
}

@MapKey
annotation class DataSourceKey(val value: DataSource)
