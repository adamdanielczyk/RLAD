package com.rlad.core.infrastructure.artic.mapper

import android.app.Application
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.infrastructure.artic.R
import com.rlad.core.infrastructure.artic.local.ArtworkEntity
import com.rlad.core.infrastructure.artic.remote.ServerArtwork
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
class ArticModelMapper(
    private val application: Application,
) : ModelMapper<ArtworkEntity, ServerArtwork> {

    override fun toLocalModel(remote: ServerArtwork): ArtworkEntity = ArtworkEntity(
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

    override fun toUiModel(local: ArtworkEntity): ItemUiModel = ItemUiModel(
        id = local.articId.toString(),
        imageUrl = "https://artic.edu/iiif/2/${local.imageId}/full/200,/0/default.jpg",
        name = local.title,
        cardCaption = local.artistDisplay ?: local.dateDisplay,
        detailsKeyValues = listOfNotNull(
            application.getString(R.string.details_title) to local.title,
            local.artistDisplay?.let { application.getString(R.string.details_artist_display) to it },
            local.placeOfOrigin?.let { application.getString(R.string.details_place_of_origin) to it },
            local.departmentTitle?.let { application.getString(R.string.details_department_title) to it },
            local.altText?.let { application.getString(R.string.details_alt_text) to it },
        ),
    )
}
