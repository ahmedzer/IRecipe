package com.za.irecipe.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.za.irecipe.R


sealed class Screens(val route: String, val icon: ImageVector, val label: String) {
    object Home: Screens("home", Icons.Filled.Home, "Welcome")
    object Saved: Screens("saved", Icons.Filled.History, "Saved")
    object RecipeScreen: Screens("recipe", Icons.Filled.Menu, "Recipe")
    object ScheduleScreen: Screens("schedule", Icons.Filled.CalendarMonth, "Schedule")
}