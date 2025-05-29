package com.za.irecipe.Data.repository

import com.za.irecipe.Data.dao.RecipeDao
import com.za.irecipe.Data.mapper.toData
import com.za.irecipe.Data.mapper.toDomain
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject


class RecipeRepositoryImpl @Inject constructor(private val recipeDao: RecipeDao): RecipeRepository {
    override suspend fun getAllRecipes(): List<RecipeModel> {
        return try {
            recipeDao.getAll().map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace() // Log l'erreur
            emptyList() // Retourne une liste vide en cas d'erreur
        }
    }

    override suspend fun getRecipeById(id: Int): RecipeModel? {
        return try {
            recipeDao.getById(id)!!.toDomain()
        } catch (e: Exception) {
            e.printStackTrace() // Log l'erreur
            null
        }
    }

    override suspend fun getPreparedRecipesForRecipe(recipeId: Int): List<PreparedRecipeModel> {
        return try {
            recipeDao.getPreparedRecipesForRecipe(recipeId).map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace() // Log l'erreur
            emptyList() // Retourne une liste vide en cas d'erreur
        }
    }

    override suspend fun insertPreparedRecipe(preparedRecipe: PreparedRecipeModel): Long {
        return try {
            recipeDao.insertPreparedRecipe(preparedRecipe.toData())
        }catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    override suspend fun getAllPreparedRecipes(): List<PreparedRecipeModel> {
        return try {
            recipeDao.getAllPreparedRecipes().map { it.toDomain() }
        }catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}