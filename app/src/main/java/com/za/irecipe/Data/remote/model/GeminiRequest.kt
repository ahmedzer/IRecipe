package com.za.irecipe.Data.remote.model

data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig = GenerationConfig()
)

data class Content(val parts: List<Part>)
data class Part(
    val text: String? = null,
    val inlineData: InlineData? = null
)

data class InlineData(
    val mimeType: String,
    val data: String
)
data class GenerationConfig(val temperature: Double = 0.7)