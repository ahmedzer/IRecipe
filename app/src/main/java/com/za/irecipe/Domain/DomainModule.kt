package com.za.irecipe.Domain

import com.za.irecipe.Domain.repository.PreparationRepository
import com.za.irecipe.Domain.repository.RecipeRepository
import com.za.irecipe.Domain.repository.VisionRepository
import com.za.irecipe.Domain.useCase.DetectObjectUseCase
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeWithRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import com.za.irecipe.Domain.useCase.GetPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetRecipeByIdUseCase
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
    fun provideDetectObjectUseCase(visionRepository: VisionRepository): DetectObjectUseCase {
        return DetectObjectUseCase(visionRepository)
    }
}