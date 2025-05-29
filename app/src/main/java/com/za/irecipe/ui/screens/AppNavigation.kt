package com.za.irecipe.ui.screens

import android.content.Context
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.za.irecipe.Domain.model.RecipeModel
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
            HomeScreen(navController  = navController)
        }
        composable(Screens.Saved.route) {
            SavedScreen()
        }
        composable(Screens.RecipeScreen.route) {
            RecipeScreen()
        }
    }
}