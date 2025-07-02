package com.za.irecipe.Domain.repository

import com.za.irecipe.Data.entities.RecipeSourceType
import com.za.irecipe.Data.entities.SavedWithRecipe
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.RecipeModel

interface RecipeRepository {

    /**
     * get all recipes from db
     * @return list of recipes
     * */
    suspend fun getAllRecipes(): List<RecipeModel>

    /***
     * get recipe by id
     * @param id id of recipe
     * @return recipe
     */
    suspend fun getRecipeById(id: Int): RecipeModel?

    /**
     * get user's prepared recipes
     * @param recipeId id of recipe
     * */
    suspend fun getPreparedRecipesForRecipe(recipeId: Int): List<PreparedRecipeModel>

    /**
     * insert prepared recipe
     * @param preparedRecipe prepared recipe
     * return id of inserted recipe
     * */
    suspend fun insertPreparedRecipe(preparedRecipe: PreparedRecipeModel): Long

    /**
     * get all prepared recipes
     * */
    suspend fun getAllPreparedRecipes(): List<PreparedRecipeModel>

    /**
     * save ai generated recipe
     * */
    suspend fun saveAiRecipe(recipe: RecipeModel, type: RecipeSourceType): Long

    /**
     * get all saved recipes
     * */
    suspend fun getAllSavedRecipes(): List<SavedWithRecipe>

    /**
     * delete saved recipe where id = recipeId
     * @param recipeId
     * */
    suspend fun deleteSavedRecipe(recipeId: Int)
}