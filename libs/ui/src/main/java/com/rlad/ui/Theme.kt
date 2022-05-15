package com.rlad.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.Random

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
fun RladTheme(content: @Composable () -> Unit) {
    val colors = if (Random().nextBoolean()) {
        DarkThemeColors
    } else {
        LightThemeColors
    }

    MaterialTheme(
        colors = colors,
        content = content,
    )
}