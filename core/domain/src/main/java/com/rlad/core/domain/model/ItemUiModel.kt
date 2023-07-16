package com.rlad.core.domain.model

data class ItemUiModel(
    val id: String,
    val imageUrl: String,
    val name: String,
    val cardCaption: String?,
    val detailsKeyValues: List<Pair<String, String>>,
)