package com.za.irecipe.Domain.repository

import com.za.irecipe.Domain.model.DetectedObject

interface VisionRepository {
    suspend fun detectObjects(imageBytes: ByteArray): List<DetectedObject>
}