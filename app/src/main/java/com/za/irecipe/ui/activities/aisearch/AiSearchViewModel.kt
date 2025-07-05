package com.za.irecipe.ui.activities.aisearch

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.za.irecipe.Data.entities.GeneratedRecipe
import com.za.irecipe.Data.entities.RecipeSourceType
import com.za.irecipe.Data.entities.SavedWithRecipe
import com.za.irecipe.Data.mapper.toData
import com.za.irecipe.Data.mapper.toDomain
import com.za.irecipe.Domain.model.DetectedObject
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.useCase.DetectObjectUseCase
import com.za.irecipe.Domain.useCase.GenerateRecipesUseCase
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import com.za.irecipe.Domain.useCase.InsertRecipeUseCase
import com.za.irecipe.ui.model.toByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AiSearchViewModel @Inject constructor(
    private val getAllRecipeUseCase: GetAllRecipeUseCase,
    private val detectObjectUseCase: DetectObjectUseCase,
    private val generateRecipesUseCase: GenerateRecipesUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase,
    application: Application
) : AndroidViewModel(application) {

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _allRecipes = MutableLiveData<List<RecipeModel>>(emptyList())
    val allRecipes: LiveData<List<RecipeModel>> get() = _allRecipes

    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap: State<Bitmap?> = _bitmap

    private val _showDetectionDialog = MutableStateFlow(false)
    val showDetectionDialog: StateFlow<Boolean> = _showDetectionDialog

    private val _detectedIngredients = MutableLiveData<List<DetectedObject>>()
    val detectedIngredients: LiveData<List<DetectedObject>> = _detectedIngredients

    private val _ingredientList = MutableStateFlow(emptyList<String>())
    val ingredientList: StateFlow<List<String>> = _ingredientList

    private val _generatedRecipes = MutableLiveData<List<GeneratedRecipe>>(emptyList())
    val generatedRecipes: LiveData<List<GeneratedRecipe>> = _generatedRecipes

    private val _isGenerationInProgress = MutableLiveData<Boolean>(false)
    val isGenerationInProgress: LiveData<Boolean> get() = _isGenerationInProgress

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private fun getAllRecipes() {
        _allRecipes.value = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                getAllRecipeUseCase.refreshData()
                val recipes = getAllRecipeUseCase.invoke()
                _allRecipes.postValue(recipes)
            } catch (e: Exception) {
                Log.d("HomeViewModel | getAllRecipes", e.message.toString())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        uri ?: return
        val context = getApplication<Application>().applicationContext
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                withContext(Dispatchers.Main) {
                    _bitmap.value = bmp
                }
            } catch (e: Exception) {
                Log.e("ImagePickerViewModel", "Failed to load bitmap: ${e.message}")
            }
        }
    }

    fun setBitmapFromCamera(bmp: Bitmap?) {
        _bitmap.value = bmp
    }

    fun showDetectionDialog() {
        _showDetectionDialog.value = true
    }

    fun closeDetectionDialog() {
        _showDetectionDialog.value = false
        _bitmap.value = null
        _detectedIngredients.value = emptyList()
        _isLoading.value = false
    }

    fun detectIngredientsFromBitmap() {
        val bmp = _bitmap.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                val result = detectObjectUseCase.invoke(bmp.toByteArray())
                Log.d("AiSearchViewModel", "Detected Ingredients: $result")
                _detectedIngredients.postValue(result)
            } catch (e: Exception) {
                Log.e("AiSearchViewModel", "Detection failed: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun removeDetectedObject(detectedObject: DetectedObject) {
        val currentList = _detectedIngredients.value.orEmpty().toMutableList()
        currentList.remove(detectedObject)
        _detectedIngredients.value = currentList
    }

    fun addIngredients(ingredients: List<String>) {
        val currentList = _ingredientList.value.toMutableList()
        currentList.addAll(ingredients)
        _ingredientList.value = currentList
    }

    fun removeIngredient(ingredient: String) {
        val currentList = _ingredientList.value.toMutableList()
        currentList.remove(ingredient)
        _ingredientList.value = currentList
    }

    fun clearAllIngredient() {
        val currentList = _ingredientList.value.toMutableList()
        currentList.clear()
        _ingredientList.value = currentList
    }

    fun generateRecipes(ingredients: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isGenerationInProgress.postValue(true)
                val generatedResult = generateRecipesUseCase.invoke(ingredients)
                _generatedRecipes.postValue(generatedResult)
            } catch (e: Exception) {
                Log.e("AiSearchViewModel", "Generation failed: ${e.message}")
            } finally {
                _isGenerationInProgress.postValue(false)
            }
        }
    }

    fun onCloseRecipeDialog() {
        _generatedRecipes.value = emptyList()
        _ingredientList.value = emptyList()
    }

    fun onSaveRecipe(recipe: GeneratedRecipe) {
        viewModelScope.launch {
            val result = try {
                withContext(Dispatchers.IO) {
                    val id = insertRecipeUseCase.invoke(recipe.toData().toDomain(), RecipeSourceType.AI)
                    if(id.toInt() != -1) {
                        showSnackbar("Recipe saved successfully")
                        getAllRecipeUseCase.refreshData()
                    }else {
                        showSnackbar("Error while saving the recipe")
                    }
                    id
                }
            } catch (e: Exception) {
                Log.e("AISearchViewModel", "Error inserting recipe", e)
                -1L
            }
            Log.e("AISearchViewModel", "$result")
        }
    }

    fun onSaveAllRecipes(recipes: List<GeneratedRecipe>) {
        viewModelScope.launch {
            try {
                insertRecipeUseCase.invoke(recipes.map { it.toData().toDomain() })
                showSnackbar("Recipes saved successfully")
            }catch (e: Exception) {
                Log.e("AISearchViewModel", "Error inserting recipes", e)
                showSnackbar("Error while saving the recipes")
            }
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }

    init {
        getAllRecipes()
    }
}