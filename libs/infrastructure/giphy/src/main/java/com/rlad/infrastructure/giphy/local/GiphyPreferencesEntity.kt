package com.rlad.infrastructure.giphy.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "giphy_preferences")
internal data class GiphyPreferencesEntity(
    @PrimaryKey @ColumnInfo(name = "preference_key") val key: String,
    @ColumnInfo(name = "value") val value: String,
)