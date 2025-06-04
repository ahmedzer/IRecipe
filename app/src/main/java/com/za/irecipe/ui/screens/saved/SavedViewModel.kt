package com.za.irecipe.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeWithRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val getAllPreparedRecipeWithRecipeUseCase: GetAllPreparedRecipeWithRecipeUseCase) :
    ViewModel() {
    private val _preparedRecipes = MutableStateFlow<List<PreparedRecipeWithRecipeModel>>(
        emptyList()
    )
    val preparedRecipes: StateFlow<List<PreparedRecipeWithRecipeModel>> get() = _preparedRecipes

    private fun getAllPreparedRecipes(refresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _preparedRecipes.value = getAllPreparedRecipeWithRecipeUseCase.invoke(refresh)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        getAllPreparedRecipes(true)
    }
}