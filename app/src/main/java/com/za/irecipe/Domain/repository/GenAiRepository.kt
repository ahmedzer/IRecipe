package com.za.irecipe.Domain.repository

import com.za.irecipe.Data.entities.GeneratedRecipe

interface GenAiRepository {
    suspend fun generateRecipes(ingredient: List<String>): List<GeneratedRecipe>
}