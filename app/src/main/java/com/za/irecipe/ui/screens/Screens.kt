package com.za.irecipe.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val icon: ImageVector, val label: String) {
    object Home: Screens("home", Icons.Filled.Home, "Home")
    object Saved: Screens("saved", Icons.Filled.Menu, "Saved")
    object RecipeScreen: Screens("recipe", Icons.Filled.Menu, "RecipeScreen")
}