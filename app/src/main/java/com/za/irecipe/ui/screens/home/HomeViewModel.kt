package com.za.irecipe.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import com.za.irecipe.Domain.useCase.GetRecipeByIdUseCase
import com.za.irecipe.ui.screens.shared.getRandomNumbersFromPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesUseCase: GetAllRecipeUseCase,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val getAllPreparedRecipeUseCase: GetAllPreparedRecipeUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _allRecipes = MutableLiveData<List<RecipeModel>>(emptyList())
    val allRecipes: LiveData<List<RecipeModel>> get() = _allRecipes

    private val _dayRecipes = MutableLiveData<List<RecipeModel?>>(emptyList())
    val dayRecipes: LiveData<List<RecipeModel?>> get() = _dayRecipes

    private val _allPreparedRecipes = MutableStateFlow<List<PreparedRecipeModel>>(emptyList())
    val allPreparedRecipes: StateFlow<List<PreparedRecipeModel>> get() = _allPreparedRecipes

    private val _selectedRecipe = MutableStateFlow<RecipeModel?>(null)
    val selectedRecipe: StateFlow<RecipeModel?> get() = _selectedRecipe

    private fun getAllRecipes() {
        _allRecipes.value = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val recipes = getRecipesUseCase.invoke()
                _allRecipes.postValue(recipes)
            } catch (e: Exception) {
                Log.d("HomeViewModel | getAllRecipes", e.message.toString())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun getAllPreparedRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val preparedRecipes = getAllPreparedRecipeUseCase.invoke()
                _allPreparedRecipes.value = preparedRecipes
            } catch (e: Exception) {
                Log.d("HomeViewModel | getAllPreparedRecipes", e.message.toString())
            }
        }
    }

    fun getDayRecipes(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ids = getRandomNumbersFromPreferences(context)
                val recipes =
                    mutableListOf<RecipeModel?>()  // Use a local list to accumulate the recipes
                ids.forEachIndexed { index, i ->
                    val recipe = getRecipeByIdUseCase.invoke(i)
                    recipes.add(recipe)  // Add the recipe to the local list
                }

                withContext(Dispatchers.Main) {
                    _dayRecipes.value = emptyList()
                    _dayRecipes.value =
                        _dayRecipes.value.orEmpty() + recipes  // Safely update the state
                }
            } catch (e: Exception) {
                Log.d("HomeViewModel | getRecipeById", e.message.toString())
            }
        }
    }

    fun openRecipeScreen(recipeModel: RecipeModel, navController: NavHostController) {
        _selectedRecipe.value = null
        _selectedRecipe.value = recipeModel

        navController.navigate("recipe") {
            popUpTo("home") {
                inclusive = false
                saveState = false
            }
            launchSingleTop = true
            restoreState = false
        }
    }

    init {
        getAllRecipes()
        getAllPreparedRecipes()
    }
}