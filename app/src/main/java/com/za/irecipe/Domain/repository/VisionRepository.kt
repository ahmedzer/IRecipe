package com.za.irecipe.Domain.repository

import com.za.irecipe.Data.entities.GeneratedRecipe
import com.za.irecipe.Domain.model.DetectedObject

interface VisionRepository {
    /**
     * detect ingredients names from image using cloud vision
     * @param imageBytes: input image
     * */
    suspend fun detectObjects(imageBytes: ByteArray): List<DetectedObject>

}