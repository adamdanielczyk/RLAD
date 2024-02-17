package com.rlad.core.infrastructure.artic.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artic_artwork",
    indices = [
        Index(value = ["artic_id"], unique = true),
    ],
)
internal data class ArtworkEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "artic_id") val articId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image_id") val imageId: String,
    @ColumnInfo(name = "artist_title") val artistTitle: String?,
    @ColumnInfo(name = "artist_display") val artistDisplay: String?,
    @ColumnInfo(name = "department_title") val departmentTitle: String?,
    @ColumnInfo(name = "place_of_origin") val placeOfOrigin: String?,
    @ColumnInfo(name = "alt_text") val altText: String?,
    @ColumnInfo(name = "date_display") val dateDisplay: String?,
)
