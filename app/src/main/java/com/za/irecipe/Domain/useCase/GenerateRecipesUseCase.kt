package com.za.irecipe.Domain.useCase

import com.za.irecipe.Data.entities.GeneratedRecipe
import com.za.irecipe.Domain.repository.GenAiRepository
import javax.inject.Inject

class GenerateRecipesUseCase @Inject constructor(
    private val genAiRepository: GenAiRepository
) {
    suspend operator fun invoke(ingredients: List<String>): List<GeneratedRecipe>{
        return genAiRepository.generateRecipes(ingredients)
    }
}