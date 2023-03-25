package com.rlad.core.infrastructure.artic.mapper

import android.app.Application
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.infrastructure.artic.R
import com.rlad.core.infrastructure.artic.local.ArtworkDataEntity
import com.rlad.core.infrastructure.artic.remote.ServerArtworkData
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import javax.inject.Inject

internal class ArticModelMapper @Inject constructor(
    private val application: Application,
) : ModelMapper<ArtworkDataEntity, ServerArtworkData> {

    override fun toLocalModel(remote: ServerArtworkData): ArtworkDataEntity = ArtworkDataEntity(
        articId = remote.id,
        title = remote.title,
        imageId = remote.imageId,
        artistTitle = remote.artistTitle,
        artistDisplay = remote.artistDisplay,
        departmentTitle = remote.departmentTitle,
        placeOfOrigin = remote.placeOfOrigin,
        altText = remote.thumbnail?.altText,
        dateDisplay = remote.dateDisplay,
    )

    override fun toUiModel(local: ArtworkDataEntity): ItemUiModel = ItemUiModel(
        id = local.articId.toString(),
        imageUrl = "https://artic.edu/iiif/2/${local.imageId}/full/400,/0/default.jpg",
        name = local.title,
        cardCaptions = listOfNotNull(
            local.artistDisplay,
            local.dateDisplay,
        ),
        detailsKeyValues = listOfNotNull(
            local.artistDisplay?.let { application.getString(R.string.details_artist_display) to it },
            local.placeOfOrigin?.let { application.getString(R.string.details_place_of_origin) to it },
            local.departmentTitle?.let { application.getString(R.string.details_department_title) to it },
            local.altText?.let { application.getString(R.string.details_alt_text) to it },
        ),
    )
}