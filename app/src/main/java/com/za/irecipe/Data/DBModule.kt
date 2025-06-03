package com.za.irecipe.Data

import android.content.Context
import com.za.irecipe.Data.dao.PreparationDao
import com.za.irecipe.Data.dao.RecipeDao
import com.za.irecipe.Data.db.AppDB
import com.za.irecipe.Data.repository.PreparationRepositoryImpl
import com.za.irecipe.Data.repository.RecipeRepositoryImpl
import com.za.irecipe.Domain.repository.PreparationRepository
import com.za.irecipe.Domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDB {
        return AppDB.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideRecipeDao(db: AppDB) = db.recipeDao()

    @Provides
    @Singleton
    fun providePreparationDao(db: AppDB) = db.preparationDao()

    @Provides
    @Singleton
    fun provideRecipeRepository(recipeDao: RecipeDao): RecipeRepository {
        return RecipeRepositoryImpl(recipeDao)
    }

    @Provides
    @Singleton
    fun providePreparationRepository(preparationDao: PreparationDao): PreparationRepository {
        return PreparationRepositoryImpl(preparationDao)
    }
}