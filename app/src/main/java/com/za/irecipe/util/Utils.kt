package com.za.irecipe.util

import android.content.Context
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import java.io.File
import java.util.Date
import java.util.Locale

fun timeStringToMinutes(timeString: String): Double {
    val parts = timeString.split(":")
    if (parts.size != 3) throw IllegalArgumentException("Invalid time format. Expected HH:mm:ss")

    val hours = parts[0].toIntOrNull() ?: 0
    val minutes = parts[1].toIntOrNull() ?: 0
    val seconds = parts[2].toIntOrNull() ?: 0

    return hours * 60 + minutes + seconds / 60.0
}

// Create image file in external files dir
fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

fun convertTimestampToDate(timestampString: String): String? {
    return try {
        val timestamp = timestampString.toLong()
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        null
    }
}