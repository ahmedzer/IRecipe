package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject

class GetPreparedRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    suspend operator fun invoke(recipeId: Int): List<PreparedRecipeModel> {
        return recipeRepository.getPreparedRecipesForRecipe(recipeId)
    }
}