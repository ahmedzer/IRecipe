package com.za.irecipe.Domain.repository

import com.za.irecipe.Data.entities.GeneratedRecipe
import com.za.irecipe.Domain.model.DetectedObject

interface GenAiRepository {
    suspend fun generateRecipes(ingredient: List<String>): List<GeneratedRecipe>
    suspend fun detectIngredientsFromImage(imageBytes: ByteArray): List<DetectedObject>
}