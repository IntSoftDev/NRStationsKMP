package com.intsoftdev.nrstations.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors =
    lightColorScheme(
        primary = Blue900,
        secondary = Blue600,
        onPrimary = Color.White,
        error = Red800
    )

private val DarkThemeColors =
    darkColorScheme(
        primary = Blue900,
        secondary = Blue600,
        onPrimary = Color.White,
        error = Red800
    )

@Composable
fun NRStationsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkThemeColors else LightThemeColors,
        // typography = ,
        //  shapes = ,
        content = content
    )
}
