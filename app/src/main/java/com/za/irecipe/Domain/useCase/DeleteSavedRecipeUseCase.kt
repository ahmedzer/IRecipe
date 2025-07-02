package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject

class DeleteSavedRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: Int) {
        recipeRepository.deleteSavedRecipe(recipeId)
    }
}