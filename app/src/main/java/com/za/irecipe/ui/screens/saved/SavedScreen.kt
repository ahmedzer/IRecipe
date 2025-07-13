package com.za.irecipe.ui.screens.saved

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.za.irecipe.ui.model.PagerTab
import com.za.irecipe.ui.screens.saved.pages.CookingHistoryPage
import com.za.irecipe.ui.screens.saved.pages.SavedRecipesScreen

@Composable
fun SavedScreen(
    navController: NavHostController
) {
    val preparationPages = listOf(
        PagerTab(title = "Cooking History", icon = Icons.Default.HistoryToggleOff, content = { CookingHistoryPage() }),
        PagerTab(title = "My Recipes", icon = Icons.Default.LocalDining, content = { SavedRecipesScreen(navController = navController) })
    )
    CustomHorizontalPager(modifier = Modifier.fillMaxSize(), tabs = preparationPages)
}