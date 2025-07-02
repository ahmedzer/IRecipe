package com.za.irecipe.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.za.irecipe.Data.entities.RecipeSourceType
import com.za.irecipe.Data.entities.SavedWithRecipe
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.useCase.DeleteSavedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import com.za.irecipe.Domain.useCase.GetRecipeByIdUseCase
import com.za.irecipe.Domain.useCase.InsertPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.InsertRecipeUseCase
import com.za.irecipe.ui.screens.shared.getRandomNumbersFromPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val getAllPreparedRecipeUseCase: GetAllPreparedRecipeUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase,
    private val deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase
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

    private val _savedRecipes = MutableStateFlow<List<SavedWithRecipe>>(emptyList())
    val savedRecipes: StateFlow<List<SavedWithRecipe>> get() = _savedRecipes

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

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

    fun getAllRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _savedRecipes.value = getRecipesUseCase.getSavedRecipes()
                getRecipesUseCase.invoke()
            } catch (e: Exception) {
                Log.d("HomeViewModel | getAllRecipes", e.message.toString())
            }
        }
    }

    fun getDayRecipes(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ids = getRandomNumbersFromPreferences(context)
                val recipes =
                    mutableListOf<RecipeModel?>()
                ids.forEachIndexed { _, i ->
                    val recipe = getRecipeByIdUseCase.invoke(i)
                    recipes.add(recipe)
                }

                withContext(Dispatchers.Main) {
                    _dayRecipes.value = emptyList()
                    _dayRecipes.value += recipes
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
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun adjustHeaderHeight(delta: Float) {
        val newHeight = (_headerHeight.value + delta).coerceIn(minHeaderHeight, maxHeaderHeight)
        _headerHeight.value = newHeight
    }

    fun resetHeader() {
        _headerHeight.value = maxHeaderHeight
    }

    fun saveRecipe(recipe: RecipeModel, recipeSourceType: RecipeSourceType) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val id = insertRecipeUseCase.invoke(recipe, recipeSourceType)
                Log.d("HomeViewModel | saveRecipe", "Recipe saved with id: $id")
            } catch (e: Exception) {
                Log.d("HomeViewModel | saveRecipe", e.message.toString())
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            getRecipesUseCase.refreshData()
            _savedRecipes.value = getRecipesUseCase.getSavedRecipes()
        }
    }

    fun deleteSavedRecipe(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSavedRecipeUseCase.invoke(recipeId)
            _savedRecipes.value = getRecipesUseCase.getSavedRecipes()
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }

    init {
        getAllPreparedRecipes()
        getAllRecipes()
    }
}