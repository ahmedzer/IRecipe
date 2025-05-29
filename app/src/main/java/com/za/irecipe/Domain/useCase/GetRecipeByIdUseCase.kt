package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(private val recipeRepository: RecipeRepository) {
    suspend operator fun invoke(id: Int): RecipeModel? {
        return recipeRepository.getRecipeById(id)
    }
}