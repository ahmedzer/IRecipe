package com.za.irecipe.ui.model

data class BannerPage(
    val title: String,
    val description: String,
    val image: Int,
    val onClick: () -> Unit
)