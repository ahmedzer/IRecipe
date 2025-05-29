package com.za.irecipe.Domain.model

class RecipeModel(
    val id: Int,
    val title: String,
    val ingredients: String,
    val instructions: String,
    val imageName: String,
    val type: String,
    val calories: Int,
    val estimatedTime: Int
) {

    fun getIngredientList(): List<String> {
        return this.ingredients
            .removeSurrounding("[", "]")
            .split("', '")
            .map { it.trim('\'') }
    }

    fun getInstructionList(): List<String> {
        return this.instructions
            .split("\n")  // Split at new lines
            .map { it.trim() }  // Trim spaces
            .filter { it.isNotEmpty() }  // Remove empty entries
    }
}