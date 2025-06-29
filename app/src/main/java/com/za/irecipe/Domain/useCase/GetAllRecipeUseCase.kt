package com.za.irecipe.Domain.useCase

import com.za.irecipe.Data.entities.SavedWithRecipe
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject

class GetAllRecipeUseCase @Inject constructor(private val recipeRepository: RecipeRepository) {

    private var cashedRecipes: List<RecipeModel>? = null
    private var savedRecipes: List<SavedWithRecipe>? = null

    suspend operator fun invoke(): List<RecipeModel> {
        return cashedRecipes?: recipeRepository.getAllRecipes().also {
            cashedRecipes = it
        }
    }

    suspend fun getSavedRecipes(): List<SavedWithRecipe> {
        return savedRecipes?: recipeRepository.getAllSavedRecipes().also {
            savedRecipes = it
        }
    }

    fun isRecipeSaved(idRecipe: Int): Boolean {
        return savedRecipes?.any { it.recipe.id_recpie == idRecipe } ?: false
    }

    suspend fun refreshData() {
        cashedRecipes = recipeRepository.getAllRecipes()
        savedRecipes = recipeRepository.getAllSavedRecipes()
    }
}