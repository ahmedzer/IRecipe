package com.za.irecipe.Data.repository

import android.util.Log
import com.za.irecipe.Data.dao.RecipeDao
import com.za.irecipe.Data.entities.RecipeSourceType
import com.za.irecipe.Data.entities.SavedRecipe
import com.za.irecipe.Data.entities.SavedWithRecipe
import com.za.irecipe.Data.mapper.toData
import com.za.irecipe.Data.mapper.toDomain
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel
import com.za.irecipe.Domain.repository.RecipeRepository
import javax.inject.Inject


class RecipeRepositoryImpl @Inject constructor(private val recipeDao: RecipeDao) :
    RecipeRepository {
    override suspend fun getAllRecipes(): List<RecipeModel> {
        return try {
            recipeDao.getAll().map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getRecipeById(id: Int): RecipeModel? {
        return try {
            recipeDao.getById(id)!!.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getPreparedRecipesForRecipe(recipeId: Int): List<PreparedRecipeModel> {
        return try {
            recipeDao.getPreparedRecipesForRecipe(recipeId).map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun insertPreparedRecipe(preparedRecipe: PreparedRecipeModel): Long {
        return try {
            recipeDao.insertPreparedRecipe(preparedRecipe.toData())
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error inserting prepared recipe: ${e.message}")
            e.printStackTrace()
            -1
        }
    }

    override suspend fun getAllPreparedRecipes(): List<PreparedRecipeModel> {
        return try {
            recipeDao.getAllPreparedRecipes().map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun saveAiRecipe(recipe: RecipeModel, type: RecipeSourceType): Long {
        return try {
            val generatedId =
                if (type == RecipeSourceType.AI) recipeDao.insertRecipe(recipe.toData())
                    .toInt() else recipe.id
            val savedRecipe = SavedRecipe(
                id_recipe = generatedId,
                saved_type = type.toString()
            )
            recipeDao.insertSavedRecipe(savedRecipe)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    override suspend fun getAllSavedRecipes(): List<SavedWithRecipe> {
        return try {
            recipeDao.getAllSavedRecipes()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun deleteSavedRecipe(recipeId: Int) {
        try {
            recipeDao.deleteSavedRecipeById(recipeId)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

}