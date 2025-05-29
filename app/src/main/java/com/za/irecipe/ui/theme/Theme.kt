package com.za.irecipe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
// Custom green colors for the theme
// Custom cappuccino colors for the theme
// Custom cappuccino colors for the theme
// Custom green colors for the theme
val CappuccinoBrown80 = Color(0xFFA57A5A)  // Rich brown
val CappuccinoBeige80 = Color(0xFFD4B996)  // Creamy beige
val CappuccinoMocha80 = Color(0xFF8B6F4E)  // Mocha brown

val CappuccinoBrown40 = Color(0xFFD4B996)  // Lighter brown
val CappuccinoBeige40 = Color(0xFFE8D9C2)  // Lighter creamy beige
val CappuccinoMocha40 = Color(0xFFB89E7D)  // Lighter mocha brown

// Define the dark color scheme
private val DarkCappuccinoColorScheme = darkColorScheme(
    primary = CappuccinoBrown80,
    secondary = CappuccinoBeige80,
    tertiary = CappuccinoMocha80,
    primaryContainer = CappuccinoBrown80.copy(alpha = 0.5f),  // Slightly transparent brown
    secondaryContainer = CappuccinoBeige80.copy(alpha = 0.5f),
    tertiaryContainer = CappuccinoMocha80.copy(alpha = 0.5f),
    surface = Color(0xFF121212),  // Default dark surface
    surfaceVariant = Color(0xFF1E1E1E),  // Default dark surface variant
    surfaceTint = CappuccinoBrown80,  // Tint for surfaces
    surfaceContainer = Color(0xFF1E1E1E),  // Surface container color
    surfaceContainerHigh = Color(0xFF2A2A2A),  // Higher elevation surface container
    surfaceContainerLow = Color(0xFF141414),  // Lower elevation surface container
    background = Color(0xFF0E0E0E),  // Default dark background
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onPrimaryContainer = Color.White,
    onSecondaryContainer = Color.Black,
    onTertiaryContainer = Color.White,
    onSurface = Color(0xFFFFFFFF),  // Light text on dark surface
    onBackground = Color(0xFFFFFFFF),  // Light text on dark background
    error = Color(0xFFCF6679),  // Default error color
    onError = Color.White,
)

// Define the light color scheme
private val LightCappuccinoColorScheme = lightColorScheme(
    primary = CappuccinoBrown40,
    secondary = CappuccinoBeige40,
    tertiary = CappuccinoMocha40,
    primaryContainer = CappuccinoBrown40.copy(alpha = 0.8f),  // Slightly transparent brown
    secondaryContainer = CappuccinoBeige40.copy(alpha = 0.8f),
    tertiaryContainer = CappuccinoMocha40.copy(alpha = 0.8f),
    surface = Color(0xFFFFFFFF),  // Default light surface
    surfaceVariant = Color(0xFFF5F5F5),  // Default light surface variant
    surfaceTint = CappuccinoBrown40,  // Tint for surfaces
    surfaceContainer = Color(0xFFF5F5F5),  // Surface container color
    surfaceContainerHigh = Color(0xFFE0E0E0),  // Higher elevation surface container
    surfaceContainerLow = Color(0xFFFAFAFA),  // Lower elevation surface container
    background = Color(0xFFFFFFFF),  // Default light background
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onPrimaryContainer = Color.Black,
    onSecondaryContainer = Color.Black,
    onTertiaryContainer = Color.Black,
    onSurface = Color(0xFF000000),  // Dark text on light surface
    onBackground = Color(0xFF000000),  // Dark text on light background
    error = Color(0xFFB00020),  // Default error color
    onError = Color.White,
)
@Composable
fun IRecipeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkCappuccinoColorScheme else LightCappuccinoColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
