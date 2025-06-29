package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.model.DetectedObject
import com.za.irecipe.Domain.repository.GenAiRepository
import javax.inject.Inject

class DetectObjectUseCase @Inject constructor(
    private val genAiRepository: GenAiRepository,
) {
    suspend fun invoke(imageBytes: ByteArray): List<DetectedObject> {
        return genAiRepository.detectIngredientsFromImage(imageBytes)
    }
}