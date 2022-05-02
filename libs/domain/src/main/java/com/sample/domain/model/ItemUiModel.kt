package com.sample.domain.model

import androidx.compose.runtime.Composable

data class ItemUiModel(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val cardCaptions: List<String>,
    val detailsKeyValues: List<Pair<@Composable () -> String, @Composable () -> String>>,
)