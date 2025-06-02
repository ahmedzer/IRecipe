package com.za.irecipe.Domain.repository

import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel

interface PreparationRepository {

    suspend fun getPreparedRecipes(): List<PreparedRecipeWithRecipeModel>

    suspend fun getPreparedRecipeWithRecipeById(preparedRecipeId: Int): PreparedRecipeWithRecipeModel?
}