package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.model.PreparedRecipeWithRecipeModel
import com.za.irecipe.Domain.repository.PreparationRepository
import javax.inject.Inject

class GetAllPreparedRecipeWithRecipeUseCase @Inject constructor(private val preparationRepository: PreparationRepository) {
    private var cashedPreparedRecipes: List<PreparedRecipeWithRecipeModel>? = null

    suspend operator fun invoke(refresh: Boolean): List<PreparedRecipeWithRecipeModel> {
        return if(refresh) {
            cashedPreparedRecipes?: preparationRepository.getPreparedRecipes().also {
                cashedPreparedRecipes = it
            }
        }else {
            preparationRepository.getPreparedRecipes().also {
                cashedPreparedRecipes = it
            }
        }
    }
}