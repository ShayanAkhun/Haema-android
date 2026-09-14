package com.viewflow.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColors = lightColorScheme(
    primary = BloodRed,
    onPrimary = SurfaceLight,
    primaryContainer = BloodRedContainerLight,
    onPrimaryContainer = BloodRedDark,
    secondary = AccentTeal,
    tertiary = AccentAmber,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    background = SurfaceLight,
    onBackground = OnSurfaceLight
)

private val DarkColors = darkColorScheme(
    primary = BloodRedLight,
    onPrimary = SurfaceDark,
    primaryContainer = BloodRedContainerDark,
    onPrimaryContainer = BloodRedContainerLight,
    secondary = AccentTeal,
    tertiary = AccentAmber,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    background = SurfaceDark,
    onBackground = OnSurfaceDark
)

@Composable
fun ViewFlowTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            systemUiController.setStatusBarColor(
                color = colorScheme.surface,
                darkIcons = useDarkIcons
            )
            systemUiController.setNavigationBarColor(
                color = colorScheme.surface,
                darkIcons = useDarkIcons
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ViewFlowTypography,
        content = content
    )
}
