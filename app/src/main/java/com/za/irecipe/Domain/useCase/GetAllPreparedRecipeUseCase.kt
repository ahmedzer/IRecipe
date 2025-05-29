package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject

class GetAllPreparedRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository) {

    private var cashedPreparedRecipes: List<PreparedRecipeModel>? = null

    suspend operator fun invoke(): List<PreparedRecipeModel> {
        return cashedPreparedRecipes?: recipeRepository.getAllPreparedRecipes().also {
            cashedPreparedRecipes = it
        }
    }
}