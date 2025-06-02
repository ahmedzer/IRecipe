package com.za.irecipe.ui.screens.preparation

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.useCase.GetRecipeByIdUseCase
import com.za.irecipe.Domain.useCase.InsertPreparedRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val insertPreparedRecipeUseCase: InsertPreparedRecipeUseCase
) : ViewModel() {

    private val _recipe = MutableStateFlow<RecipeModel?>(null)
    val recipe: StateFlow<RecipeModel?> = _recipe

    private val _insertionResult = MutableStateFlow<Long?>(null)
    val insertionResult: StateFlow<Long?> get() = _insertionResult

    private var secondsElapsed = 0
    private var isRunning = false

    private val _formattedTime = MutableStateFlow("00:00:00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap: State<Bitmap?> = _bitmap

    fun setBitmapFromCamera(bmp: Bitmap?) {
        _bitmap.value = bmp
    }

    fun openInsertionDialog() {
        _showDialog.value = true
    }

    fun closeInsertionDialog() {
        _showDialog.value = false
    }

    fun startTimer() {
        if (isRunning) return
        isRunning = true
        viewModelScope.launch {
            while (isRunning) {
                delay(1000)
                secondsElapsed++
                _formattedTime.value = formatTime(secondsElapsed)
            }
        }
    }

    fun stopTimer() {
        isRunning = false
    }

    private fun resetTimer() {
        stopTimer()
        secondsElapsed = 0
        _formattedTime.value = "00:00:00"
    }

    private fun formatTime(seconds: Int): String {
        val hours = TimeUnit.SECONDS.toHours(seconds.toLong())
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong()) % 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }


    fun fetchRecipe(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipeModel = getRecipeByIdUseCase.invoke(recipeId)
                _recipe.value = recipeModel
            } catch (e: Exception) {
                Log.e("PreparationViewModel", "Error fetching recipe", e)
            }
        }
    }

    fun savePreparedRecipe(preparedRecipe: PreparedRecipeModel) {
        viewModelScope.launch {
            val result = try {
                withContext(Dispatchers.IO) {
                    insertPreparedRecipeUseCase.invoke(preparedRecipe)
                }
            } catch (e: Exception) {
                Log.e("PreparationViewModel", "Error inserting prepared recipe", e)
                -1L
            }
            _insertionResult.value = result
            Log.e("PreparationViewModel", "$result")
        }
    }

    fun resetInsertionResult() {
        _insertionResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("PreparationViewModel", "ViewModel cleared")
        clearRecipe()
        stopTimer()
    }

    init {
        resetTimer()
        startTimer()
    }

    fun clearRecipe() {
        _recipe.value = null
    }
}