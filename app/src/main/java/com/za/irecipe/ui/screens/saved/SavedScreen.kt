package com.za.irecipe.ui.screens.saved

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.storage.FirebaseStorage


@Composable
fun loadImageUrl(imageName: String): State<String?> {
    val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
    val imageUrlState = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(imageName) {
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                imageUrlState.value = uri.toString()
            }
            .addOnFailureListener {
                Log.e("FirebaseStorage", "Error: ${it.message}")
            }
    }
    return imageUrlState
}
@Composable
fun SavedScreen() {
    val imageUrl = "spiral-ham-in-the-slow-cooker-guarnaschelli.jpg"
    Text(
        text = "Item",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )

    //LoadImageFromName(imageUrl)
}