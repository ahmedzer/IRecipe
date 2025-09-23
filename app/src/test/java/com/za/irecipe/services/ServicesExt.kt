package com.za.irecipe.services

import com.za.irecipe.Data.remote.model.GeminiRequest
import com.za.irecipe.Data.remote.model.GeminiResponse
import com.za.irecipe.Data.remote.services.GeminiApiService
import kotlinx.coroutines.runBlocking

fun GeminiApiService.generateContentBlocking(
    apiKey: String,
    request: GeminiRequest
): GeminiResponse = runBlocking {
    generateContent(apiKey, request)
}