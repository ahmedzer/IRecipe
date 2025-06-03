package com.za.irecipe.Data.repository

import com.za.irecipe.Data.dao.PreparationDao
import com.za.irecipe.Data.mapper.toDomain
import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel
import com.za.irecipe.Domain.repository.PreparationRepository
import javax.inject.Inject

class PreparationRepositoryImpl @Inject constructor(private val preparationDao: PreparationDao): PreparationRepository {
    override suspend fun getPreparedRecipes(): List<PreparedRecipeWithRecipeModel> {
        return try {
            preparationDao.getAllPreparedRecipesWithRecipe().map { it.toDomain() }
        }catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPreparedRecipeWithRecipeById(preparedRecipeId: Int): PreparedRecipeWithRecipeModel? {
        return try {
            preparationDao.getPreparedRecipeWithRecipeById(preparedRecipeId)?.toDomain()
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}