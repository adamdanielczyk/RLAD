package com.rlad.shared.data.common.model

enum class DataSource(val dataSourceName: String) {
    ARTIC("ARTIC"),
    GIPHY("GIPHY"),
    RICKANDMORTY("RICK_AND_MORTY");

    companion object {
        fun fromString(dataSourceName: String) = entries.first { it.dataSourceName == dataSourceName }
    }
}