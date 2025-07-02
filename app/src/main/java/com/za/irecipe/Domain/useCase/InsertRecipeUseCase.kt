package com.za.irecipe.Domain.useCase

import com.za.irecipe.Data.entities.RecipeSourceType
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject

class InsertRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipe: RecipeModel, type: RecipeSourceType): Long {
        return recipeRepository.saveAiRecipe(recipe, type)
    }
}