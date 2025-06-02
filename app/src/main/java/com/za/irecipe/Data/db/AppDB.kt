package com.za.irecipe.Data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.za.irecipe.Data.dao.RecipeDao
import com.za.irecipe.Data.entities.PreparedRecipe
import com.za.irecipe.Data.entities.Recipe
import java.io.File
import java.io.FileOutputStream


@Database(entities = [Recipe::class, PreparedRecipe::class], version = 2, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getInstance(context: Context): AppDB {
            return INSTANCE ?: synchronized(this) {
                val dbFile = context.getDatabasePath("database.db")

                // Delete existing database (for testing purposes)
                if (!dbFile.exists()) {
                    copyDatabaseFromAssets(context, "database.db", dbFile)
                    Log.d("Database", "Database copied from assets")
                } else {
                    Log.d("Database", "Database exists, skipping copy")
                }

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java, "database.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE!!
            }
        }

        // Fonction pour copier la base de données depuis les assets
        private fun copyDatabaseFromAssets(context: Context, assetName: String, outputFile: File) {
            try {
                context.assets.open(assetName).use { input ->
                    FileOutputStream(outputFile).use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d("Database", "Base de données copiée depuis les assets vers ${outputFile.absolutePath}")
            } catch (e: Exception) {
                Log.e("Database", "Erreur lors de la copie de la base de données depuis les assets", e)
            }
        }
    }
}

// Migration pour la version 1 → 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Ajoutez les nouvelles colonnes
        db.execSQL("ALTER TABLE recipes ADD COLUMN Type TEXT DEFAULT ''")
        db.execSQL("ALTER TABLE recipes ADD COLUMN Calories INTEGER DEFAULT 0")
        db.execSQL("ALTER TABLE recipes ADD COLUMN Estimated_Time INTEGER DEFAULT 0")
        Log.d("Migration", "Migration de la version 1 à 2 appliquée avec succès")
    }
}