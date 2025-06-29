package com.za.irecipe.Data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "saved_recipes",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id_recpie"],
            childColumns = ["id_recipe"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SavedRecipe(
    @PrimaryKey(autoGenerate = true) val id_saved: Int = 0,
    val id_recipe: Int,
    val saved_type: String, // should be "AI" or "Manual"
    val saved_at: Long = System.currentTimeMillis()
)

data class SavedWithRecipe(
    @Embedded val savedRecipe: SavedRecipe,

    @Relation(
        parentColumn = "id_recipe",
        entityColumn = "id_recpie"
    )
    val recipe: Recipe
)
