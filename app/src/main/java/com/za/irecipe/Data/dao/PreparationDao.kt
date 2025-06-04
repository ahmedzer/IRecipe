package com.za.irecipe.Data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.za.irecipe.Data.entities.PreparedRecipe
import com.za.irecipe.Data.entities.PreparedRecipeWithRecipe

@Dao
interface PreparationDao {
    @Transaction
    @Query("SELECT * FROM prepared_recipe")
    suspend fun getAllPreparedRecipesWithRecipe(): List<PreparedRecipeWithRecipe>

    @Transaction
    @Query("SELECT * FROM prepared_recipe WHERE id_prep = :preparedRecipeId")
    suspend fun getPreparedRecipeWithRecipeById(preparedRecipeId: Int): PreparedRecipeWithRecipe?
}