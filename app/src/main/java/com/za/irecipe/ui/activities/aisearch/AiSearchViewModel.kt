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
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AiSearchViewModel @Inject constructor(
    private val getAllRecipeUseCase: GetAllRecipeUseCase,
    application: Application
): AndroidViewModel(application) {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _allRecipes = MutableLiveData<List<RecipeModel>>(emptyList())
    val allRecipes: LiveData<List<RecipeModel>> get() = _allRecipes

    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap: State<Bitmap?> = _bitmap

    private fun getAllRecipes() {
        _allRecipes.value = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
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

    init {
        getAllRecipes()
    }
}