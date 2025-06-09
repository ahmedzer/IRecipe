package com.za.irecipe.Domain.useCase

import com.za.irecipe.Domain.model.DetectedObject
import com.za.irecipe.Domain.repository.VisionRepository
import javax.inject.Inject

class DetectObjectUseCase @Inject constructor(
    private val visionRepository: VisionRepository
) {
    suspend fun invoke(imageBytes: ByteArray): List<DetectedObject> {
        return visionRepository.detectObjects(imageBytes)
    }
}