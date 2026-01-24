package com.rlad.core.infrastructure.giphy.mapper

import android.app.Application
import com.rlad.core.domain.model.ItemUiModel
import com.rlad.core.infrastructure.common.mapper.ModelMapper
import com.rlad.core.infrastructure.giphy.R
import com.rlad.core.infrastructure.giphy.local.GifEntity
import com.rlad.core.infrastructure.giphy.remote.ServerGif
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding

@ContributesBinding(AppScope::class)
class GiphyModelMapper(
    private val application: Application,
) : ModelMapper<GifEntity, ServerGif> {

    override fun toLocalModel(remote: ServerGif): GifEntity = GifEntity(
        giphyId = remote.id,
        title = remote.title,
        imageUrl = remote.images.fixedWidth.url,
        username = remote.username,
        importDatetime = remote.importDatetime,
        trendingDatetime = remote.trendingDatetime,
    )

    override fun toUiModel(local: GifEntity): ItemUiModel = ItemUiModel(
        id = local.giphyId,
        imageUrl = local.imageUrl,
        name = local.title,
        cardCaption = local.username,
        detailsKeyValues = listOf(
            application.getString(R.string.details_title) to local.title,
            application.getString(R.string.details_username) to local.username,
            application.getString(R.string.details_import_date) to local.importDatetime,
            application.getString(R.string.details_trending_date) to local.trendingDatetime,
        ),
    )
}
