package com.za.irecipe.Domain.model

data class PreparedRecipeModel(
    val id_prep: Int? = 0,
    val id_recipe: Int?,
    val preparationTime: Double?,
    val imagePath: String?,
    val preparationDay: String?
)

data class PreparedRecipeWithRecipeModel(
    val preparedRecipeModel: PreparedRecipeModel,
    val recipeModel: RecipeModel?
)