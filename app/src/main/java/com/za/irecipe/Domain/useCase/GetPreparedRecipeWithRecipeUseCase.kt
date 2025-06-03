package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.repository.PreparationRepository
import javax.inject.Inject

class GetPreparedRecipeWithRecipeUseCase @Inject constructor(private val preparationRepository: PreparationRepository) {

    suspend operator fun invoke(preparedRecipeId: Int) = preparationRepository.getPreparedRecipeWithRecipeById(preparedRecipeId)

}