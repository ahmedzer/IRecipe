package com.za.irecipe.ui.screens.shared

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = MaterialTheme.colorScheme.background
        ),
    ) {
        Text(text)
    }
}