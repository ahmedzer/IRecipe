package com.za.irecipe.Domain.model

data class PreparedRecipeModel(
    val id_prep: Int? = 0,
    val id_recipe: Int?, // This must match the parent column type
    val preparationTime: Double?,
    val imagePath: String?,
    val preparationDay: String?
)