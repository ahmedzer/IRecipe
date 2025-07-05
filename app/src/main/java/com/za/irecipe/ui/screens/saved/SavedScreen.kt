package com.za.irecipe.ui.screens.saved

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.za.irecipe.ui.model.PagerTab
import com.za.irecipe.ui.screens.saved.pages.CookingHistoryPage
import com.za.irecipe.ui.screens.saved.pages.SavedRecipesScreen

@Composable
fun SavedScreen() {
    val preparationPages = listOf(
        PagerTab(title = "Cooking History", icon = Icons.Default.HistoryToggleOff, content = { CookingHistoryPage() }),
        PagerTab(title = "My Recipes", icon = Icons.Default.LocalDining, content = { SavedRecipesScreen() })
    )
    CustomHorizontalPager(modifier = Modifier.fillMaxSize(), tabs = preparationPages)
}