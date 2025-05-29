package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject

class GetAllRecipeUseCase @Inject constructor(private val recipeRepository: RecipeRepository) {

    private var cashedRecipes: List<RecipeModel>? = null

    suspend operator fun invoke(): List<RecipeModel> {
        return cashedRecipes?: recipeRepository.getAllRecipes().also {
            cashedRecipes = it
        }
    }
}