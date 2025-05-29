package com.za.irecipe.Domain.repository

import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel

interface RecipeRepository {

    suspend fun getAllRecipes(): List<RecipeModel>

    suspend fun getRecipeById(id: Int): RecipeModel?

    suspend fun getPreparedRecipesForRecipe(recipeId: Int): List<PreparedRecipeModel>

    suspend fun insertPreparedRecipe(preparedRecipe: PreparedRecipeModel): Long

    suspend fun getAllPreparedRecipes(): List<PreparedRecipeModel>
}