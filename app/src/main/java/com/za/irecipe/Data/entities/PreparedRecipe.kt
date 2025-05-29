package com.za.irecipe.Data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "prepared_recipe",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id_recpie"],
            childColumns = ["id_recipe"],
            onDelete = ForeignKey.CASCADE // Automatically deletes related rows
        )
    ]
)
data class PreparedRecipe(
    @PrimaryKey(autoGenerate = true)
    val id_prep: Int?,
    @ColumnInfo(name = "id_recipe")  // Update the column name to match the parent column name
    val idRecipe: Int?,
    @ColumnInfo(name = "preparation_time")
    val preparationTime: Double?,
    @ColumnInfo(name = "image_path")
    val imagePath: String?,
    @ColumnInfo(name = "preparation_day")
    val preparationDay: String?
)