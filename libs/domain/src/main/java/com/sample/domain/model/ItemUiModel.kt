package com.sample.domain.model

data class ItemUiModel(
    val id: String,
    val imageUrl: String,
    val name: String,
    val cardCaptions: List<String>,
    val detailsKeyValues: List<Pair<String, String>>,
)