package com.za.irecipe.ui.screens.saved

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.za.irecipe.ui.model.PagerTab
import com.za.irecipe.ui.screens.saved.pages.CookingHistoryPage

@Composable
fun SavedScreen(
    viewModel: SavedViewModel = hiltViewModel()
) {
    val preparationPages = listOf(
        PagerTab(title = "Cooking History", icon = Icons.Default.HistoryToggleOff, content = { CookingHistoryPage() }),
        PagerTab(title = "My Recipes", icon = Icons.Default.LocalDining, content = { CookingHistoryPage() })
    )
    CustomHorizontalPager(modifier = Modifier.fillMaxSize(), tabs = preparationPages)
}