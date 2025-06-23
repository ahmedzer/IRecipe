package com.za.irecipe.Data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id_recpie: Int,
    val Title: String,
    val Ingredients: String,
    val Instructions: String,
    val Image_Name: String,
    val Type: String,
    val Calories: Int,
    val Estimated_Time: Int
)

data class GeneratedRecipe(
    val Title: String,
    val Ingredients: String,
    val Instructions: String,
    val Type: String,
    val Calories: Int,
    val Estimated_Time: Int
)