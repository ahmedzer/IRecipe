package com.za.irecipe.Data.mapper

import com.za.irecipe.Data.entities.PreparedRecipe
import com.za.irecipe.Data.entities.PreparedRecipeWithRecipe
import com.za.irecipe.Data.entities.Recipe
import com.za.irecipe.Domain.model.PreparedRecipeModel
import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel
import com.za.irecipe.Domain.model.RecipeModel

fun Recipe.toDomain(): RecipeModel {
    return RecipeModel(
        id = this.id_recpie,
        title = this.Title,
        ingredients = this.Ingredients,
        instructions = this.Instructions,
        imageName = this.Image_Name,
        type = this.Type,
        calories = this.Calories,
        estimatedTime = this.Estimated_Time
    )
}

fun RecipeModel.toData(): Recipe {
    return Recipe(
        id_recpie = this.id,
        Title = this.title,
        Ingredients = this.ingredients,
        Instructions = this.instructions,
        Image_Name = this.imageName,
        Type = this.type,
        Calories = this.calories,
        Estimated_Time = this.estimatedTime

    )
}

fun PreparedRecipe.toDomain(): PreparedRecipeModel {
    return PreparedRecipeModel(
        id_prep = this.id_prep,
        id_recipe = this.idRecipe,
        preparationTime = this.preparationTime,
        imagePath = this.imagePath,
        preparationDay = this.preparationDay
    )
}

fun PreparedRecipeModel.toData(): PreparedRecipe {
    return PreparedRecipe(
        id_prep = null,
        idRecipe = this.id_recipe,
        preparationTime = this.preparationTime,
        imagePath = this.imagePath,
        preparationDay = this.preparationDay
    )
}

fun PreparedRecipeWithRecipe.toDomain(): PreparedRecipeWithRecipeModel {
    return PreparedRecipeWithRecipeModel(
        preparedRecipeModel = this.preparedRecipe.toDomain(),
        recipeModel = this.recipe?.toDomain()
    )
}

fun PreparedRecipeWithRecipeModel.toData(): PreparedRecipeWithRecipe {
    return PreparedRecipeWithRecipe(
        preparedRecipe = this.preparedRecipeModel.toData(),
        recipe = this.recipeModel?.toData()
    )
}