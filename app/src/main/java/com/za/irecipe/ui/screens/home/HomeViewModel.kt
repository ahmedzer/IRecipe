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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecipesUseCase: GetAllRecipeUseCase,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val getAllPreparedRecipeUseCase: GetAllPreparedRecipeUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _dayRecipes = MutableStateFlow<List<RecipeModel?>>(emptyList())
    val dayRecipes: StateFlow<List<RecipeModel?>> get() = _dayRecipes

    private val _allPreparedRecipes = MutableStateFlow<List<PreparedRecipeModel>>(emptyList())
    val allPreparedRecipes: StateFlow<List<PreparedRecipeModel>> get() = _allPreparedRecipes

    private val _selectedRecipe = MutableStateFlow<RecipeModel?>(null)
    val selectedRecipe: StateFlow<RecipeModel?> get() = _selectedRecipe

    //nested scroll handle
    private val _headerHeight = MutableStateFlow(200f)
    val headerHeight: StateFlow<Float> = _headerHeight.asStateFlow()

    private val maxHeaderHeight = 200f
    private val minHeaderHeight = 30f

    val isCollapsed = headerHeight.map { it <= 150f }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

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

    fun adjustHeaderHeight(delta: Float) {
        val newHeight = (_headerHeight.value + delta).coerceIn(minHeaderHeight, maxHeaderHeight)
        _headerHeight.value = newHeight
    }

    init {
        getAllPreparedRecipes()
    }
}