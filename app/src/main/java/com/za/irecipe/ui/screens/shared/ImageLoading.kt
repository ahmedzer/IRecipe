package com.za.irecipe.ui.screens.shared

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.firebase.storage.FirebaseStorage

@Composable
fun loadImageUrl(imageName: String): State<String?> {
    val imageUrlState = rememberSaveable(imageName) { mutableStateOf<String?>(null) }

    LaunchedEffect(imageName) {
        if (imageUrlState.value == null) {
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
            storageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    imageUrlState.value = uri.toString()
                }
                .addOnFailureListener {
                    Log.e("FirebaseStorage", "Error loading image: ${it.message}")
                }
        }
    }

    return imageUrlState
}

@Composable
fun LoadImageFromName(
    imageName: String,
    size: Size,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageUrl by loadImageUrl(imageName)

    val request = imageUrl?.let {
        ImageRequest.Builder(context)
            .data(it)
            .crossfade(true)
            .memoryCacheKey("images/$imageName")
            .diskCacheKey("images/$imageName")
            .build()
    }

    // Coil 3: returns StateFlow<AsyncImagePainter.State>
    val painter = rememberAsyncImagePainter(model = request)

    // to avoid multiple reloading
    val painterState by painter.state.collectAsState()

    Box(
        modifier = modifier
            .size(size.width.dp, size.height.dp)
            .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
    ) {
        when (painterState) {
            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                //dark overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )

            }

            is AsyncImagePainter.State.Loading,
            is AsyncImagePainter.State.Error,
            is AsyncImagePainter.State.Empty -> {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
