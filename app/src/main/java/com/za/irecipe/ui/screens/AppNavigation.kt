package com.za.irecipe.ui.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.za.irecipe.ui.screens.home.HomeScreen
import com.za.irecipe.ui.screens.recipeScreen.RecipeScreen
import com.za.irecipe.ui.screens.saved.SavedScreen


@Composable
fun AppBottomNav(
    navController: NavHostController,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,

        ) {
        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.Saved.route) {
            SavedScreen()
        }
        composable(Screens.RecipeScreen.route) {
            RecipeScreen()
        }
    }
}