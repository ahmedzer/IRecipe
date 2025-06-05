package com.za.irecipe.ui.screens.saved.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.za.irecipe.ui.screens.saved.SavedViewModel
import com.za.irecipe.ui.screens.shared.PreparedRecipeCard

@Composable
fun CookingHistoryPage(viewModel: SavedViewModel = hiltViewModel()) {
    val preparedRecipes by viewModel.preparedRecipes.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = if (preparedRecipes.isNotEmpty()) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (preparedRecipes.isNotEmpty()) {
            preparedRecipes.forEachIndexed { index, preparedRecipe ->
                item(key = index) {
                    PreparedRecipeCard(preparedRecipeWithRecipeModel = preparedRecipe, onClick = {})
                }
            }
        } else {
            item {
                Icon(
                    imageVector = Icons.Default.RestoreFromTrash,
                    contentDescription = "Empty Icon",
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}