package com.za.irecipe.Data.remote.model

data class VisionRequest(
    val requests: List<RequestItem>
)

data class RequestItem(
    val image: ImageContent,
    val features: List<Feature>
)

data class ImageContent(
    val content: String  // base64-encoded image
)

data class Feature(
    val type: String = "OBJECT_LOCALIZATION"
)

data class VisionResponse(
    val responses: List<ResponseItem>
)

data class ResponseItem(
    val localizedObjectAnnotations: List<DetectedObject>?
)

data class DetectedObject(
    val name: String,
    val score: Float
)