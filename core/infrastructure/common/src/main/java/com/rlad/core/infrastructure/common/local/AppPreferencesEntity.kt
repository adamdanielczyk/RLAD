package com.rlad.core.infrastructure.common.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_preferences")
data class AppPreferencesEntity(
    @PrimaryKey @ColumnInfo(name = "preference_key") val key: String,
    @ColumnInfo(name = "value") val value: String,
)