package com.intsoftdev.nrservices.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.intsoftdev.nrstations.app.ui.theme.Blue600
import com.intsoftdev.nrstations.app.ui.theme.Blue900
import com.intsoftdev.nrstations.app.ui.theme.Red800

private val LightThemeColors = lightColors(
    primary = Blue900,
    primaryVariant = Blue600,
    onPrimary = Color.White,
    error = Red800
)

private val DarkThemeColors = darkColors(
    primary = Blue900,
    primaryVariant = Blue600,
    onPrimary = Color.White,
    error = Red800
)

@Composable
fun NRStationsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        // typography = ,
        //  shapes = ,
        content = content
    )
}
