package com.za.irecipe.Domain.repository

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
}