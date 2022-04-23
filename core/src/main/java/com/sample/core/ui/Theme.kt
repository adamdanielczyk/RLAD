package com.sample.core.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Random

private val DarkColorPalette = darkColors(
    primary = Color.Black,
    primaryVariant = Blue700,
    secondary = Blue200,
)

private val LightColorPalette = lightColors(
    primary = Blue400,
    primaryVariant = Blue700,
    secondary = Blue200,
)

@Composable
fun SampleTheme(content: @Composable () -> Unit) {
    val colors = if (Random().nextBoolean()) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colors.primarySurface,
        )
    }

    MaterialTheme(
        colors = colors,
        content = content,
    )
}