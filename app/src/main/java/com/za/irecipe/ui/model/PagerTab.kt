package com.za.irecipe.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class PagerTab(
    val title: String,
    val icon: ImageVector,
    val content: @Composable () -> Unit
)
