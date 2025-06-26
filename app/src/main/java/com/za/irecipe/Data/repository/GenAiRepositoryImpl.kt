package com.za.irecipe.Data.repository

import com.za.irecipe.Data.entities.GeneratedRecipe
import com.za.irecipe.Data.remote.services.GeminiApiService
import com.za.irecipe.Domain.repository.GenAiRepository
import javax.inject.Inject

class GenAiRepositoryImpl @Inject constructor(
    private val geminiApiService: GeminiApiService
): GenAiRepository {
    override suspend fun generateRecipes(ingredient: List<String>): List<GeneratedRecipe> {
        TODO("Not yet implemented")
    }

    private fun buildPrompt(ingredients: List<String>): String {
        return """
            I have these ingredients: ${ingredients.joinToString(", ")}.
            Give me 3 recipes as JSON array, each like:
            {
              "Title": "Pasta",
              "Ingredients": "100g pasta\n1 tsp salt",
              "Instructions": "Step 1...\nStep 2...",
              "Image_Name": null,
              "Type": "Main Course",
              "Calories": 250,
              "Estimated_Time": 15
            }
        """.trimIndent()
    }
}