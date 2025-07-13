package com.za.irecipe.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.za.irecipe.Data.entities.SavedWithRecipe
import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeWithRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getAllPreparedRecipeWithRecipeUseCase: GetAllPreparedRecipeWithRecipeUseCase,
    private val getAllRecipeUseCase: GetAllRecipeUseCase
) : ViewModel() {
    private val _preparedRecipes = MutableStateFlow<List<PreparedRecipeWithRecipeModel>>(
        emptyList()
    )
    val preparedRecipes: StateFlow<List<PreparedRecipeWithRecipeModel>> get() = _preparedRecipes

    private val _savedRecipes = MutableStateFlow<List<SavedWithRecipe>>(emptyList())
    val savedRecipes: StateFlow<List<SavedWithRecipe>> get() = _savedRecipes

    private val _selectedRecipe = MutableStateFlow<RecipeModel?>(null)
    val selectedRecipe: StateFlow<RecipeModel?> get() = _selectedRecipe

    private fun getAllPreparedRecipes(refresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _preparedRecipes.value = getAllPreparedRecipeWithRecipeUseCase.invoke(refresh)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSavedRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _savedRecipes.value = getAllRecipeUseCase.getSavedRecipes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun openRecipeScreen(recipeModel: RecipeModel, navController: NavHostController) {
        _selectedRecipe.value = null
        _selectedRecipe.value = recipeModel

        navController.navigate("recipe") {
            popUpTo("saved") {
                inclusive = false
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    init {
        getAllPreparedRecipes(true)
        getSavedRecipes()
    }
}