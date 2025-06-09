package com.za.irecipe.Data.remote.services

import com.za.irecipe.Data.remote.model.VisionRequest
import com.za.irecipe.Data.remote.model.VisionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface VisionApiService {
    @POST("v1/images:annotate")
    suspend fun annotateImage(
        @Query("key") apiKey: String,
        @Body request: VisionRequest
    ): VisionResponse
}