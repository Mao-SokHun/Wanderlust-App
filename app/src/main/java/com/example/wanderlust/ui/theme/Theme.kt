package com.example.wanderlust.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ─────────────────────────────────────────────────────────────────────────────
// Wanderlust — Nature Explorer Theme
// Primary  : Emerald Green  (trust, nature, Cambodia)
// Secondary: Warm Amber     (energy, sunshine, star ratings)
// Tertiary : Sky Blue       (water, sky, travel)
// ─────────────────────────────────────────────────────────────────────────────

private val DarkScheme = darkColorScheme(
    // ── Primary (Emerald) ─────────────────────────────────────────────────────
    primary                = WanderlustDark.Primary,
    onPrimary              = WanderlustDark.OnPrimaryContainer,
    primaryContainer       = WanderlustDark.PrimaryContainer,
    onPrimaryContainer     = WanderlustDark.OnPrimaryContainer,

    // ── Secondary (Amber) ─────────────────────────────────────────────────────
    secondary              = WanderlustDark.Secondary,
    onSecondary            = WanderlustDark.OnSecondaryContainer,
    secondaryContainer     = WanderlustDark.SecondaryContainer,
    onSecondaryContainer   = WanderlustDark.OnSecondaryContainer,

    // ── Tertiary (Sky Blue) ───────────────────────────────────────────────────
    tertiary               = WanderlustDark.Tertiary,
    onTertiary             = WanderlustDark.OnTertiaryContainer,
    tertiaryContainer      = WanderlustDark.TertiaryContainer,
    onTertiaryContainer    = WanderlustDark.OnTertiaryContainer,

    // ── Surface & Background ──────────────────────────────────────────────────
    background             = WanderlustDark.Background,
    onBackground           = WanderlustDark.OnBackground,
    surface                = WanderlustDark.Surface,
    onSurface              = WanderlustDark.OnSurface,
    surfaceVariant         = WanderlustDark.SurfaceContainerHigh,
    onSurfaceVariant       = WanderlustDark.OnSurfaceVariant,
    surfaceContainer       = WanderlustDark.SurfaceContainer,
    surfaceContainerHigh   = WanderlustDark.SurfaceContainerHigh,
    surfaceContainerLow    = WanderlustDark.SurfaceContainerLow,

    // ── Outline ───────────────────────────────────────────────────────────────
    outline                = WanderlustDark.Outline,
    outlineVariant         = WanderlustDark.OutlineVariant,

    // ── Error ─────────────────────────────────────────────────────────────────
    error                  = WanderlustDark.Error,
    onError                = WanderlustDark.OnError,
)

private val LightScheme = lightColorScheme(
    // ── Primary (Emerald) ─────────────────────────────────────────────────────
    primary                = WanderlustLight.Primary,
    onPrimary              = Color.White,
    primaryContainer       = WanderlustLight.PrimaryContainer,
    onPrimaryContainer     = WanderlustLight.OnPrimaryContainer,

    // ── Secondary (Amber) ─────────────────────────────────────────────────────
    secondary              = WanderlustLight.Secondary,
    onSecondary            = Color.White,
    secondaryContainer     = WanderlustLight.SecondaryContainer,
    onSecondaryContainer   = WanderlustLight.OnSecondaryContainer,

    // ── Tertiary (Sky Blue) ───────────────────────────────────────────────────
    tertiary               = WanderlustLight.Tertiary,
    onTertiary             = Color.White,
    tertiaryContainer      = WanderlustLight.TertiaryContainer,
    onTertiaryContainer    = WanderlustLight.OnTertiaryContainer,

    // ── Surface & Background ──────────────────────────────────────────────────
    background             = WanderlustLight.Background,
    onBackground           = WanderlustLight.OnBackground,
    surface                = WanderlustLight.Surface,
    onSurface              = WanderlustLight.OnSurface,
    surfaceVariant         = WanderlustLight.SurfaceContainerHigh,
    onSurfaceVariant       = WanderlustLight.OnSurfaceVariant,
    surfaceContainer       = WanderlustLight.SurfaceContainer,
    surfaceContainerHigh   = WanderlustLight.SurfaceContainerHigh,
    surfaceContainerLow    = WanderlustLight.SurfaceContainerLow,

    // ── Outline ───────────────────────────────────────────────────────────────
    outline                = WanderlustLight.Outline,
    outlineVariant         = WanderlustLight.OutlineVariant,

    // ── Error ─────────────────────────────────────────────────────────────────
    error                  = WanderlustLight.Error,
    onError                = WanderlustLight.OnError,
)

@Composable
fun WanderlustTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val scheme = if (darkTheme) DarkScheme else LightScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = scheme,
        typography = WanderlustTypography,
        content = content,
    )
}
