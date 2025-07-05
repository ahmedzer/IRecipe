package com.za.irecipe.ui.screens.saved.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.za.irecipe.Data.entities.RecipeSourceType
import com.za.irecipe.Data.mapper.toDomain
import com.za.irecipe.ui.screens.saved.SavedViewModel
import com.za.irecipe.ui.screens.shared.RecipeCard

@Composable
fun SavedRecipesScreen(
    savedViewModel: SavedViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        savedViewModel.getSavedRecipes()
    }

    val savedRecipes by savedViewModel.savedRecipes.collectAsState()

    val aiRecipes = savedRecipes.filter { it.savedRecipe.saved_type == RecipeSourceType.AI.toString() }
    val manualRecipes = savedRecipes.filter { it.savedRecipe.saved_type == RecipeSourceType.Manual.toString() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Text(
                text = "AI Recipes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(aiRecipes.size) { index ->
                    RecipeCard(
                        recipe = aiRecipes[index].recipe.toDomain(),
                        isSaved = true,
                        onSave = {},
                        onCardClick = {},
                        showActions = false
                    )
                }
            }
        }

        item {
            Text(
                text = "My saved Recipes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(manualRecipes.size) { index ->
                    RecipeCard(
                        recipe = manualRecipes[index].recipe.toDomain(),
                        isSaved = true,
                        onSave = {},
                        onCardClick = {},
                        showActions = false
                    )
                }
            }
        }
    }
}