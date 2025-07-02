package com.za.irecipe.Domain

import com.za.irecipe.Domain.repository.GenAiRepository
import com.za.irecipe.Domain.repository.PreparationRepository
import com.za.irecipe.Domain.repository.RecipeRepository
import com.za.irecipe.Domain.useCase.DeleteSavedRecipeUseCase
import com.za.irecipe.Domain.useCase.DetectObjectUseCase
import com.za.irecipe.Domain.useCase.GenerateRecipesUseCase
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeWithRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import com.za.irecipe.Domain.useCase.GetPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetRecipeByIdUseCase
import com.za.irecipe.Domain.useCase.InsertRecipeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetAllRecipeUseCase(recipeRepository: RecipeRepository): GetAllRecipeUseCase {
        return GetAllRecipeUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipeByIdUseCase(recipeRepository: RecipeRepository): GetRecipeByIdUseCase {
        return GetRecipeByIdUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetPreparedRecipeUseCase(recipeRepository: RecipeRepository): GetPreparedRecipeUseCase {
        return GetPreparedRecipeUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllPreparedRecipeUseCase(recipeRepository: RecipeRepository): GetAllPreparedRecipeUseCase {
        return GetAllPreparedRecipeUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllUserPreparedRecipes(preparedRepository: PreparationRepository): GetAllPreparedRecipeWithRecipeUseCase {
        return GetAllPreparedRecipeWithRecipeUseCase(preparedRepository)
    }

    @Provides
    @Singleton
    fun provideDetectObjectUseCase(genAiRepository: GenAiRepository): DetectObjectUseCase {
        return DetectObjectUseCase(genAiRepository)
    }

    @Provides
    @Singleton
    fun provideGenerateRecipesUseCase(genAiRepository: GenAiRepository): GenerateRecipesUseCase {
        return GenerateRecipesUseCase(genAiRepository)
    }

    @Provides
    @Singleton
    fun provideInsertRecipeUseCase(recipeRepository: RecipeRepository): InsertRecipeUseCase {
        return InsertRecipeUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteSavedRecipeUseCase(recipeRepository: RecipeRepository): DeleteSavedRecipeUseCase {
        return DeleteSavedRecipeUseCase(recipeRepository)
    }

}