package com.sample.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Random

private val DarkThemeColors = darkColors(
    primary = Color.Black,
    primaryVariant = Blue700,
    secondary = Blue200,
)

private val LightThemeColors = lightColors(
    primary = Blue400,
    primaryVariant = Blue700,
    secondary = Blue200,
)

@Composable
fun SampleTheme(content: @Composable () -> Unit) {
    val colors = if (Random().nextBoolean()) {
        DarkThemeColors
    } else {
        LightThemeColors
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