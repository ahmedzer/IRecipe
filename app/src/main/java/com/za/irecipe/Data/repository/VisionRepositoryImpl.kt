package com.za.irecipe.Data.repository

import android.util.Log
import com.za.irecipe.Data.remote.model.Feature
import com.za.irecipe.Data.remote.model.ImageContent
import com.za.irecipe.Data.remote.model.RequestItem
import com.za.irecipe.Data.remote.model.VisionRequest
import com.za.irecipe.Data.remote.services.VisionApiService
import com.za.irecipe.Domain.model.DetectedObject
import com.za.irecipe.Domain.repository.VisionRepository
import java.util.Base64
import com.za.irecipe.BuildConfig
import javax.inject.Inject

class VisionRepositoryImpl @Inject constructor(
    private val visionApiService: VisionApiService
) : VisionRepository {
    override suspend fun detectObjects(imageBytes: ByteArray): List<DetectedObject> {
        val base64Image = Base64.getEncoder().encodeToString(imageBytes)
        val request = VisionRequest(
            requests = listOf(
                RequestItem(
                    image = ImageContent(content = base64Image),
                    features = listOf(Feature(type = "OBJECT_LOCALIZATION"))
                )
            )
        )

        try {
            val response = visionApiService.annotateImage(BuildConfig.GOOGLE_VISION_API_KEY, request)

            val detectedDtos = response.responses.firstOrNull()?.localizedObjectAnnotations ?: emptyList()
            return detectedDtos.map { DetectedObject(it.name, it.score) }

        } catch (e: retrofit2.HttpException) {
            // Erreur HTTP (403, 401, etc)
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("VisionRepositoryImpl", "HTTP error ${e.code()}: $errorBody")
            throw e
        } catch (e: Exception) {
            Log.e("VisionRepositoryImpl", "Unexpected error: ${e.message}")
            throw e
        }
    }

}