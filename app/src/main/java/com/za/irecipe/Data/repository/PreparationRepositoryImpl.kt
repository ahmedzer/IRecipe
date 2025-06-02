package com.za.irecipe.Data.repository

import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel
import com.za.irecipe.Domain.repository.PreparationRepository

class PreparationRepositoryImpl: PreparationRepository {
    override suspend fun getPreparedRecipes(): List<PreparedRecipeWithRecipeModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getPreparedRecipeWithRecipeById(preparedRecipeId: Int): PreparedRecipeWithRecipeModel? {
        TODO("Not yet implemented")
    }
}