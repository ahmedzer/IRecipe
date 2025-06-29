package com.za.irecipe.Data.repository

import android.util.Log
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.za.irecipe.BuildConfig
import com.za.irecipe.Data.entities.GeneratedRecipe
import com.za.irecipe.Data.remote.model.Content
import com.za.irecipe.Data.remote.model.GeminiRequest
import com.za.irecipe.Data.remote.model.InlineData
import com.za.irecipe.Data.remote.model.Part
import com.za.irecipe.Data.remote.services.GeminiApiService
import com.za.irecipe.Domain.model.DetectedObject
import com.za.irecipe.Domain.repository.GenAiRepository
import javax.inject.Inject

class GenAiRepositoryImpl @Inject constructor(
    private val geminiApiService: GeminiApiService
): GenAiRepository {

    override suspend fun generateRecipes(ingredient: List<String>): List<GeneratedRecipe> {
        return try {
            val prompt = buildPrompt(ingredient)
            val request = GeminiRequest(
                contents = listOf(Content(parts = listOf(Part(prompt))))
            )

            val response = geminiApiService.generateContent(
                BuildConfig.GOOGLE_VISION_API_KEY,
                request
            )

            val rawText = response.candidates.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text ?: throw Exception("No response from Gemini")

            val jsonText = rawText
                .replace("```json", "")
                .replace("```", "")
                .trim()

            Log.d("Generated result", jsonText)

            val type = object : TypeToken<List<GeneratedRecipe>>() {}.type
            return Gson().fromJson(jsonText, type)

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun buildPrompt(ingredients: List<String>): String {
        return """
            I have these ingredients: ${ingredients.joinToString(", ")}.
            Give me 3 recipes as JSON array, each like:
            {
              "Title": "Pasta",
              "Ingredients": "100g pasta\n1 tsp salt",
              "Instructions": "Step 1...\nStep 2...",
              "Image_Name": null,
              "Type": "Main Course",
              "Calories": 250,
              "Estimated_Time": 15
            }
        """.trimIndent()
    }

    override suspend fun detectIngredientsFromImage(imageBytes: ByteArray): List<DetectedObject> {
        return try {
            val base64Image =
                android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP)

            val promptPart =
                Part(text = "List all food ingredients visible in this image with confidence scores as JSON array like [{\"name\":\"Tomato\", \"score\":0.92}].")
            val imagePart = Part(
                inlineData = InlineData(
                    mimeType = "image/jpeg",
                    data = base64Image
                )
            )

            val request = GeminiRequest(
                contents = listOf(Content(parts = listOf(promptPart, imagePart)))
            )

            val response = geminiApiService.generateContent(
                BuildConfig.GOOGLE_VISION_API_KEY,
                request
            )

            val rawText = response.candidates.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text ?: throw Exception("No response from Gemini")

            val jsonText = rawText
                .replace("```json", "")
                .replace("```", "")
                .trim()

            Log.d("DetectedObjects", jsonText)

            val type = object : TypeToken<List<DetectedObject>>() {}.type
            Gson().fromJson(jsonText, type)

        } catch (e: Exception) {
            Log.e("GenAiRepositoryImpl", "Failed to detect ingredients: ${e.message}")
            emptyList()
        }
    }
}