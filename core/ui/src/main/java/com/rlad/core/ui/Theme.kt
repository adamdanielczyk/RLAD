package com.rlad.core.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkThemeColors = darkColors(
    primary = Color.Black,
    primaryVariant = Gray900,
    secondary = Yellow200,
)

private val LightThemeColors = lightColors(
    primary = Gray800,
    primaryVariant = Gray900,
    secondary = Yellow400,
)

@Composable
fun RladTheme(
    useDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }

    MaterialTheme(
        colors = colors,
        content = content,
    )
}