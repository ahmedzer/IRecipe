package com.za.irecipe.ui.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun PopupDetailedMessage(
    message: String,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current
    val context = LocalContext.current
    val screenHeightPx = with(density) {
        context.resources.displayMetrics.heightPixels.toDp()
    }

    Popup(
        onDismissRequest = onDismiss,
        offset = IntOffset(0, with(density) { screenHeightPx.roundToPx() - 150 }),
        properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(text = message, color = MaterialTheme.colorScheme.background)
        }
    }
}