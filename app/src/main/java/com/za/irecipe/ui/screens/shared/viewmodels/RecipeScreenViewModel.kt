package com.za.irecipe.ui.screens.shared.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.za.irecipe.Domain.model.RecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeScreenViewModel  @Inject constructor(): ViewModel() {
    private val _selectedRecipe = MutableStateFlow<RecipeModel?>(null)
    val selectedRecipe: StateFlow<RecipeModel?> = _selectedRecipe

    fun openRecipeScreen(recipeModel: RecipeModel, navController: NavHostController) {
        _selectedRecipe.value = null
        _selectedRecipe.value = recipeModel

        navController.navigate("recipe") {
            popUpTo("home") {
                inclusive = false
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}