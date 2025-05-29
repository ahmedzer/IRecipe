package com.za.irecipe.ui.di

import com.za.irecipe.Domain.useCase.GetAllPreparedRecipeUseCase
import com.za.irecipe.Domain.useCase.GetAllRecipeUseCase
import com.za.irecipe.Domain.useCase.GetRecipeByIdUseCase
import com.za.irecipe.Domain.useCase.InsertPreparedRecipeUseCase
import com.za.irecipe.ui.screens.home.HomeViewModel
import com.za.irecipe.ui.screens.preparation.PreparationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {
    @Provides
    @Singleton
    fun provideHomeViewModel(
        getRecipesUseCase: GetAllRecipeUseCase,
        getRecipeByIdUseCase: GetRecipeByIdUseCase,
        getAllPreparedRecipeUseCase: GetAllPreparedRecipeUseCase
    ): HomeViewModel {

        return HomeViewModel(getRecipesUseCase, getRecipeByIdUseCase, getAllPreparedRecipeUseCase)
    }

    @Provides
    fun providePreparationViewModel(
        getRecipeByIdUseCase: GetRecipeByIdUseCase,
        insertPreparedRecipeUseCase: InsertPreparedRecipeUseCase
    ): PreparationViewModel {

        return PreparationViewModel(getRecipeByIdUseCase, insertPreparedRecipeUseCase)
    }
}