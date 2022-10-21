package com.rlad.core.infrastructure.common.mapper

import com.rlad.core.domain.model.ItemUiModel

interface ModelMapper<LocalModel, RemoteModel> {

    fun toLocalModel(remote: RemoteModel): LocalModel
    fun toUiModel(local: LocalModel): ItemUiModel
}