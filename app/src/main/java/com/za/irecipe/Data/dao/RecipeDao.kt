package com.za.irecipe.Data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.za.irecipe.Data.entities.PreparedRecipe
import com.za.irecipe.Data.entities.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id_recpie = :id")
    fun getById(id: Int): Recipe?

    @Transaction
    @Query("SELECT * FROM prepared_recipe WHERE id_recipe = :recipeId")
    fun getPreparedRecipesForRecipe(recipeId: Int): List<PreparedRecipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreparedRecipe(preparedRecipe: PreparedRecipe): Long

    @Query("SELECT * FROM prepared_recipe")
    fun getAllPreparedRecipes(): List<PreparedRecipe>
}